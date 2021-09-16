package com.example.expensetrackerapi.controller;

import java.util.Map;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.jwtgenerator.JWTGenerator;
import com.example.expensetrackerapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT + GlobalConstants.USERS_PATH)
/**
 * A REST controller which handles User registration/login related
 * functionalities
 */
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userDetails) {

        String firstName = (String) userDetails.get("firstName");
        String lastName = (String) userDetails.get("lastName");
        String email = (String) userDetails.get("email");
        String password = (String) userDetails.get("password");
        UserEntity user = userService.registerUser(firstName, lastName, email, password);
        Map<String, String> map = JWTGenerator.getJWTGenerator().generateJWT(user);
        return ResponseEntity.ok(map);

    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userDetails) {

        String email = (String) userDetails.get("email");
        String password = (String) userDetails.get("password");
        // validate user
        UserEntity user = userService.validateUser(email, password);

        // Generate JWT for the logged-in user
        Map<String, String> map = JWTGenerator.getJWTGenerator().generateJWT(user);
        return ResponseEntity.ok(map);

    }
}
