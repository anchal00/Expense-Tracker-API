package com.example.expensetrackerapi.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.UserEntity;
import com.example.expensetrackerapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
        Map<String, String> map = generateJWT(user);
        return ResponseEntity.ok(map);
        
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> loginUser( @RequestBody Map<String, Object> userDetails) {

        String email = (String) userDetails.get("email");
        String password = (String) userDetails.get("password");
        UserEntity user = userService.validateUser(email, password);
        Map<String, String> map  = generateJWT(user);
        return ResponseEntity.ok(map);

    }

    private  Map<String, String> generateJWT(UserEntity user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256 , GlobalConstants.API_SECRET_KEY)
            .setIssuedAt(new Date(timestamp))
            .setExpiration(new Date(timestamp + GlobalConstants.TOKEN_VALIDITY))
            .claim("userId", user.getUserId())
            .claim("email", user.getEmail())
            .claim("firstName", user.getFirstName())
            .claim("lastName", user.getLastName())
            .compact();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }
    
}
