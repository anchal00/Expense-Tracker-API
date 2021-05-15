package com.example.expensetrackerapi.service;

import java.util.regex.Pattern;

import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.exception.ExpTrackException;
import com.example.expensetrackerapi.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserEntity registerUser(String firstName, String lastName, String email, String password)
            throws ExpTrackException {

        if (!isEmailValid(email)) {
            throw new ExpTrackException("Invalid email format");
        }

        if (userRepository.getCountByEmail(email) > 0) {
            throw new ExpTrackException("Email is already in use");
        }
        Integer userId = userRepository.createUser(firstName, lastName, email, password);
        return userRepository.findById(userId);
    }

    @Override
    public UserEntity validateUser(String email, String password) throws ExpTrackException {

        UserEntity user = userRepository.findByEmailAndPassword(email, password);
        if (!BCrypt.checkpw(password, user.getPassword())){
            throw new ExpTrackException("Invalid email/password");
        }
        return user;
    }

    public boolean isEmailValid(String email) {
        Pattern patternForEmail = Pattern.compile("^(.+)@(.+)$");
        if (email != null)
            email = email.toLowerCase();

        if (!patternForEmail.matcher(email).matches()) {
            return false;
        }
        return true;

    }
}
