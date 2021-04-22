package com.example.expensetrackerapi.service;

import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.exception.ExpTrackException;

public interface UserService {

    public UserEntity registerUser(String firstName, String lastName, String email, String password)
            throws ExpTrackException;

    public UserEntity validateUser(String email, String password) throws ExpTrackException;
}
