package com.example.expensetrackerapi.jwtgenerator;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JWTGenerator {

    private static JWTGenerator jwtGenerator;

    private JWTGenerator() {}

    public static JWTGenerator getJWTGenerator() {
        if (jwtGenerator == null) {
            jwtGenerator = new JWTGenerator();
        }
        return jwtGenerator;
    }

    public Map<String, String> generateJWT(UserEntity user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, GlobalConstants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp)).setExpiration(new Date(timestamp + GlobalConstants.TOKEN_VALIDITY))
                .claim("userId", user.getUserId()).claim("email", user.getEmail())
                .claim("firstName", user.getFirstName()).claim("lastName", user.getLastName()).compact();
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return tokenMap;
    }
}
