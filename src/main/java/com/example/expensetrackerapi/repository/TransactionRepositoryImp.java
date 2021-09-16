package com.example.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImp implements TransactionRepository {

    private static final String CREATE_SQL = "INSERT INTO ETRACKER_TRANSACTIONS (TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE , TRANSACTION_DATETIMESTAMP) VALUES(NEXTVAL('ETRACKER_TRANSACTIONS_SEQ'), ? , ?, ? ,? , ?)";

    private static final String FIND_BY_ID_SQL = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE , TRANSACTION_DATETIMESTAMP FROM ETRACKER_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ? ";

    private static final String FIND_ALL_SQL = "SELECT TRANSACTION_ID,CATEGORY_ID, USER_ID, AMOUNT,NOTE, TRANSACTION_DATETIMESTAMP FROM ETRACKER_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ?";

    private static final String UPDATE_SQL = "UPDATE ETRACKER_TRANSACTIONS SET AMOUNT = ? , NOTE = ?, TRANSACTION_DATETIMESTAMP = ? WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ? ";

    private static final String DELETE_BY_ID_SQL = "DELETE FROM ETRACKER_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
        Transaction transaction = new Transaction(rs.getInt("TRANSACTION_ID"), rs.getInt("CATEGORY_ID"),
                rs.getInt("USER_ID"), rs.getDouble("AMOUNT"), rs.getString("NOTE"), rs.getTimestamp("TRANSACTION_DATETIMESTAMP"));

        return transaction;

    });

    @Override
    public List<Transaction> findAll(Integer userId, Integer CategoryId) {

        return jdbcTemplate.query(FIND_ALL_SQL, new Object[] { userId, CategoryId }, transactionRowMapper);
    }

    @Override
    public Transaction findById(Integer userId, Integer CategoryId, Integer transactionId)
            throws EtResourceNotFoundException {

        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[] { userId, CategoryId, transactionId },
                    transactionRowMapper);
        } catch (Exception e) {
            throw new EtBadRequestException("Transaction not found ! ");
        }
    }

    @Override
    public Integer create(Integer userId, Integer CategoryId, Double amount, String note, LocalDateTime transactionDate)
            throws EtBadRequestException {

        Timestamp timestamp = Timestamp.valueOf(transactionDate);
        try {

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, CategoryId);
                stmt.setInt(2, userId);
                stmt.setDouble(3, amount);
                stmt.setString(4, note);
                stmt.setTimestamp(5, timestamp);
                return stmt;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("TRANSACTION_ID");

        } catch (Exception e) {
            throw new EtBadRequestException("Invalid Request");
        }
    }

    @Override
    public void update(Integer userId, Integer CategoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {

        try {
            jdbcTemplate.update(UPDATE_SQL, new Object[] { transaction.getAmount(), transaction.getNote(),
                    transaction.getTransactionDate(), userId, CategoryId, transactionId });
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid Request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer CategoryId, Integer transactionId)
            throws EtResourceNotFoundException {

        int countOfRowsDeleted = jdbcTemplate.update(DELETE_BY_ID_SQL,
                new Object[] { userId, CategoryId, transactionId });

        if (countOfRowsDeleted == 0) {
            throw new EtResourceNotFoundException("Transaction doesn't exit");
        }
    }

}
