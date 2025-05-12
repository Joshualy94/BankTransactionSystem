package com.bank.banksystem.stresstest;

import com.bank.banksystem.model.Transaction;
import com.bank.banksystem.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TransactionStressTest {

    @Autowired
    private TransactionService service;

    private static final int THREAD_COUNT = 100;
    private static final int REQUEST_PER_THREAD = 10;

    @Test
    void testHighConcurrencyCreateTransactions() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT * REQUEST_PER_THREAD; i++) {
            executor.submit(() -> {
                Transaction t = new Transaction();
                t.setAmount(new BigDecimal("100.00"));
                t.setType("STRESS_TEST");
                service.createTransaction(t);
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Total time for " + (THREAD_COUNT * REQUEST_PER_THREAD) +
                " requests: " + duration + " ms");
    }
}