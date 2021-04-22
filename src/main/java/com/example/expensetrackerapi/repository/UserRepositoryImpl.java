package com.example.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.exception.ExpTrackException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String CREATE_USER = "INSERT INTO"
            + " ETRACKER_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)"
            + " VALUES(NEXTVAL('ETRACKER_USERS_SEQ'), ? ,? , ?, ?)";

    private static final String COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ETRACKER_USERS WHERE EMAIL = ?";

    private static final String FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD "
            + "FROM ETRACKER_USERS WHERE USER_ID = ?";
            
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer createUser(String firstName, String lastName, String email, String password)
            throws ExpTrackException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement prepStatement = connection.prepareStatement(CREATE_USER,
                        Statement.RETURN_GENERATED_KEYS);
                prepStatement.setString(1, firstName);
                prepStatement.setString(2, lastName);
                prepStatement.setString(3, email);
                prepStatement.setString(4, password);
                return prepStatement;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");

        } catch (Exception e) {
            throw new ExpTrackException("Invalid Details , Failed to create an account");
        }
    }

    @Override
    public UserEntity findByEmailAndPassword(String email, String password) throws ExpTrackException {
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {

        return jdbcTemplate.queryForObject(COUNT_BY_EMAIL, new Object[] { email }, Integer.class);
    }

    @Override
    public UserEntity findById(Integer userId) {

        return jdbcTemplate.queryForObject(FIND_BY_ID, new Object[] { userId }, userRowMapper);
    }

    private RowMapper<UserEntity> userRowMapper = ((rs, rowNum) -> {

        return new UserEntity(rs.getInt("USER_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
                rs.getString("EMAIL"), rs.getString("PASSWORD"));
    });
}
