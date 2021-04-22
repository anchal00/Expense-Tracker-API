package com.example.expensetrackerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpTrackException extends RuntimeException{

    public ExpTrackException(String message) {
        super(message);
    }
}
