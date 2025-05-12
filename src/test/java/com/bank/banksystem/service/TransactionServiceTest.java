package com.bank.banksystem.service;

import com.bank.banksystem.exception.DuplicateTransactionException;
import com.bank.banksystem.exception.TransactionNotFoundException;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId("123");
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType("DEPOSIT");
    }

    @Test
    void createTransaction_Success() {
        when(repository.existsById(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(transaction);

        Transaction result = service.createTransaction(transaction);
        assertNotNull(result.getId());
        assertEquals("DEPOSIT", result.getType());
        verify(repository, times(1)).save(transaction);
    }

    @Test
    void createTransaction_Duplicate_ThrowsException() {
        when(repository.existsById("123")).thenReturn(true);
        assertThrows(DuplicateTransactionException.class, () ->
                service.createTransaction(transaction));
    }

    @Test
    void deleteTransaction_NotFound_ThrowsException() {
        when(repository.existsById("456")).thenReturn(false);
        assertThrows(TransactionNotFoundException.class, () ->
                service.deleteTransaction("456"));
    }

    @Test
    void getAllTransactions_Pagination() {
        when(repository.findAll()).thenReturn(List.of(transaction));
        List<Transaction> result = service.getAllTransactions(0, 10);
        assertEquals(1, result.size());
    }
}
