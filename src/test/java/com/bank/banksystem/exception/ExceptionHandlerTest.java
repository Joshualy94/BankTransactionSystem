package com.bank.banksystem.exception;

import com.bank.banksystem.controller.TransactionController;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class ExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @Test
    void handleDuplicateTransaction_Returns409() throws Exception {
        Transaction t = new Transaction();
        t.setId("1");
        t.setAmount(new BigDecimal(100));
        t.setType("DEPOSIT");
        when(service.createTransaction(any(Transaction.class))).thenThrow(new DuplicateTransactionException("Duplicate"));

        mockMvc.perform(post("/api/transactions")
                        .contentType("application/json")
                        .content("{\"id\":\"1\",\"amount\":100.00,\"type\":\"DEPOSIT\"}"))
                .andExpect(status().isConflict());
        System.out.println("pass");
    }

    @Test
    void handleTransactionNotFound_Returns404() throws Exception {
        Transaction t = new Transaction();
        t.setId("1");
        t.setAmount(new BigDecimal(100));
        t.setType("DEPOSIT");
        service.createTransaction(t);
        doThrow(new TransactionNotFoundException("Not found"))
                .when(service).deleteTransaction("999");

        mockMvc.perform(delete("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }
}