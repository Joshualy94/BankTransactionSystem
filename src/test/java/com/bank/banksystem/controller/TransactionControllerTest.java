package com.bank.banksystem.controller;

import com.bank.banksystem.exception.TransactionNotFoundException;
import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService service;

    @Test
    void createTransaction_ValidRequest_Returns201() throws Exception {
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId("1");
        mockTransaction.setAmount(new BigDecimal("100.00"));
        mockTransaction.setType("DEPOSIT");

        when(service.createTransaction(any())).thenReturn(mockTransaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.00,\"type\":\"DEPOSIT\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void getTransactions_ValidPagination_Returns200() throws Exception {
        mockMvc.perform(get("/api/transactions?page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTransaction_NotFound_Returns404() throws Exception {
        doThrow(new TransactionNotFoundException("Not found"))
                .when(service).deleteTransaction("999");
        mockMvc.perform(delete("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }
}
