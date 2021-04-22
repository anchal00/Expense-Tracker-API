package com.example.expensetrackerapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT + GlobalConstants.USERS_PATH)
public class UserController {
    
    @Autowired
    UserService userService;

    @PostMapping(value="/register")
    public ResponseEntity<Map<String, String>> registerUser( @RequestBody Map<String, Object> userDetails) {
        
        String firstName = (String) userDetails.get("firstName");
        String lastName = (String) userDetails.get("lastName");
        String email = (String) userDetails.get("email");
        String password = (String) userDetails.get("password");
        UserEntity user = userService.registerUser(firstName, lastName, email, password);
        Map<String, String> map = new HashMap<>();

        map.put("message", "registered successfully");
        return ResponseEntity.ok(map);
        
    }
    
}
