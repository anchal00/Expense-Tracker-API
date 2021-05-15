package com.example.expensetrackerapi.repository;

import java.util.List;

import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

public interface TransactionRepository {

    List<Transaction> findAll(Integer userId, Integer CategoryId);

    Transaction findById(Integer userId, Integer CategoryId, Integer transactionId) throws EtResourceNotFoundException;

    Integer create(Integer userId, Integer CategoryId, Double amount, String note, Long transactionDate)
            throws EtBadRequestException;

    void update(Integer userId, Integer CategoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException;

    void removeById(Integer userId, Integer CategoryId, Integer transactionId) throws EtResourceNotFoundException;
}
