package com.example.expensetrackerapi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.example.expensetrackerapi.entity.Category;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String CREATE_SQL = "INSERT INTO ETRACKER_CATEGORIES(CATEGORY_ID , USER_ID, TITLE, DESCRIPTION) "
    
    +"VALUES(NEXTVAL('ETRACKER_CATEGORIES_SEQ'), ? ,? ,?)";

    private static final String FIND_BY_ID_SQL = "SELECT C.CATEGORY_ID , C.USER_ID, C.TITLE, C.DESCRIPTION, "
    + "COALESCE(SUM(T.AMOUNT),0) TOTAL_EXPENSE "
    + "FROM ETRACKER_TRANSACTIONS T RIGHT OUTER JOIN ETRACKER_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " 
    + "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID";

    private static final String FIND_ALL_SQL = "SELECT C.CATEGORY_ID , C.USER_ID, C.TITLE, C.DESCRIPTION, "
    + "COALESCE(SUM(T.AMOUNT),0) TOTAL_EXPENSE "
    + "FROM ETRACKER_TRANSACTIONS T RIGHT OUTER JOIN ETRACKER_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " 
    + "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID";

    private static final String UPDATE_CATEGORY_SQL = "UPDATE ETRACKER_CATEGORIES SET TITLE = ? , DESCRIPTION = ? "
    + "WHERE USER_ID = ? AND CATEGORY_ID = ?";

    private static final String GET_CATEGORY_COUNT_SQL = "SELECT COUNT(*) FROM ETRACKER_CATEGORIES WHERE TITLE = ";

    private static final String DELETE_CATEGORY_SQL = "DELETE FROM ETRACKER_CATEGORIES WHERE USER_ID = ? AND CATEGORY_ID = ?";
    
    private static final String DELETE_ALL_CATEGORY_TRANSACTIONS_SQL = "DELETE FROM ETRACKER_TRANSACTIONS WHERE CATEGORY_ID = ?";

    @Override
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {

        return jdbcTemplate.query(FIND_ALL_SQL, new Object[] {userId},categoryRowMapper);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        try {
            
            return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, new Object[]{userId, categoryId}, categoryRowMapper);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String desc) throws EtBadRequestException {

        if (categoryExists(title, userId)){
            throw new EtBadRequestException("The Category with same title exists for this user !");
        }

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement statement = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, userId);
                statement.setString(2, title);
                statement.setString(3, desc);
                return statement;

            } , keyHolder);

            return (Integer) keyHolder.getKeys().get("CATEGORY_ID");
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {

        jdbcTemplate.update( new PreparedStatementCreator(){

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                
                PreparedStatement stmt = con.prepareStatement(UPDATE_CATEGORY_SQL, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, category.getTitle());
                stmt.setString(2, category.getDescription());
                stmt.setInt(3, userId);
                stmt.setInt(4, categoryId);
                return stmt;
            }
            
        });
    }

    private void deleteAllTransactionsForCategory(Integer categoryId) {
        jdbcTemplate.update(DELETE_ALL_CATEGORY_TRANSACTIONS_SQL, new Object[]{categoryId});
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {

        deleteAllTransactionsForCategory(categoryId);
        jdbcTemplate.update(DELETE_CATEGORY_SQL, new Object[]{userId, categoryId});
    }

    private RowMapper<Category> categoryRowMapper = ( (rs, rowNum) -> {

        return new Category(rs.getInt("CATEGORY_ID") ,
            rs.getInt("USER_ID") , 
            rs.getString("TITLE") ,
            rs.getString("DESCRIPTION") ,
            rs.getDouble("TOTAL_EXPENSE"));
    } );

    public boolean categoryExists(String name, Integer userId) {
    
        Integer id = jdbcTemplate.queryForObject(GET_CATEGORY_COUNT_SQL+ "'"+name+"'", Integer.class);

        if (id == null || id == 0) {
            return false;
        }
        return true;
    }

}
