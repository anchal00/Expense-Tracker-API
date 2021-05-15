package com.example.expensetrackerapi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT+"/categories/{categoryId}/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction( HttpServletRequest req , 
            @PathVariable("categoryId") Integer categoryId , 
            @RequestBody Map<String, Object> transactionMap) {
        
        Integer userId = (Integer) req.getAttribute("userId");
        Double amount = Double.valueOf(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Long transactionDate = (Long) transactionMap.get("transactionDate");

        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        
    }
    
}
