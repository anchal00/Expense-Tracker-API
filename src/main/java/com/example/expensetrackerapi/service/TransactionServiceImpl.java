package com.example.expensetrackerapi.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.expensetrackerapi.entity.Transaction;
import com.example.expensetrackerapi.exception.EtBadRequestException;
import com.example.expensetrackerapi.exception.EtResourceNotFoundException;
import com.example.expensetrackerapi.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
       
        return transactionRepository.findAll(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {
        
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
            LocalDateTime transactionDate) throws EtBadRequestException {
                
        Integer id = transactionRepository.create(userId, categoryId, amount, note, transactionDate);

        return transactionRepository.findById(userId, categoryId,id);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws EtBadRequestException {

        transactionRepository.update(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws EtResourceNotFoundException {

        transactionRepository.removeById(userId, categoryId, transactionId);
    }
    
}
