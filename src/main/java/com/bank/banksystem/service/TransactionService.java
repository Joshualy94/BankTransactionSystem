package com.bank.banksystem.service;

import com.bank.banksystem.exception.DuplicateTransactionException;
import com.bank.banksystem.exception.TransactionNotFoundException;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.repository.TransactionRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID().toString());
        }
        if (repository.existsById(transaction.getId())) {
            throw new DuplicateTransactionException("Transaction ID already exists");
        }
        transaction.setTimestamp(LocalDateTime.now());
        return repository.save(transaction);
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction updateTransaction(String id, Transaction transaction) {
        if (!repository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction not found with ID: " + id);
        }
        transaction.setId(id);
        return repository.save(transaction);
    }

    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(String id) {
        if (!repository.existsById(id)) {
            throw new TransactionNotFoundException("Transaction not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    @Cacheable("transactions")
    public List<Transaction> getAllTransactions(int page, int size) {
        List<Transaction> allTransactions = repository.findAll();
        int start = page * size;
        int end = Math.min(start + size, allTransactions.size());
        return allTransactions.subList(start, end);
    }
}