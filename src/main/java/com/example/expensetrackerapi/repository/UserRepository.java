package com.example.expensetrackerapi.repository;

import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.exception.ExpTrackException;

public interface UserRepository {

    Integer createUser(String firstName, String lastName, String email, String password) throws ExpTrackException;

    UserEntity findByEmail(String email) throws ExpTrackException;

    Integer getCountByEmail(String email);
    
    UserEntity findById(Integer userId);

}
