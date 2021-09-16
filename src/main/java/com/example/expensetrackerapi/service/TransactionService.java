package com.example.expensetrackerapi.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;

/**
 * Transaction service interface contract
 */
public interface TransactionService {

        List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

        Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
                        throws EtResourceNotFoundException;

        Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, LocalDateTime transactionDate)
                        throws EtBadRequestException;

        void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
                        throws EtBadRequestException;

        void removeTransactionById(Integer userId, Integer categoryId, Integer transactionId)
                        throws EtResourceNotFoundException;
}
