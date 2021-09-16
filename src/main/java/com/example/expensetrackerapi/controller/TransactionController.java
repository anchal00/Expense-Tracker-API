package com.example.expensetrackerapi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.expensetrackerapi.constants.GlobalConstants;
import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GlobalConstants.API_ENDPOINT + "/categories/{categoryId}/transactions")
/**
 * A REST controller for managing transactions for a expense category
 * 
 */
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    public ResponseEntity<List<Transaction>> getTransactionById(HttpServletRequest request,
            @PathVariable("categoryId") Integer categoryId) {

        Integer userId = (Integer) request.getAttribute("userId");

        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);

        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @GetMapping("{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest request,
            @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId) {

        Integer userId = (Integer) request.getAttribute("userId");

        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);

        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest req,
            @PathVariable("categoryId") Integer categoryId, @RequestBody Map<String, Object> transactionMap) {

        Integer userId = (Integer) req.getAttribute("userId");
        Double amount = Double.valueOf(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        String date = (String) transactionMap.get("transactionDate");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime transactionDate = LocalDateTime.parse(date, formatter);

        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);

    }

    @PutMapping("{transactionId}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
            @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId,
            @RequestBody Transaction transaction) {

        Integer userId = (Integer) request.getAttribute("userId");

        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);

        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    }

    @DeleteMapping("{transactionId}")
    public ResponseEntity<Map<String, Boolean>> deleteTransaction(HttpServletRequest request,
            @PathVariable("categoryId") Integer categoryId, @PathVariable("transactionId") Integer transactionId) {

        Integer userId = (Integer) request.getAttribute("userId");

        transactionService.removeTransactionById(userId, categoryId, transactionId);

        return new ResponseEntity<>(Map.of("Success", true), HttpStatus.OK);
    }

}
