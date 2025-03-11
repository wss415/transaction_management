package com.hsbc.transaction.application.usecase.query.transaction.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.hsbc.transaction.application.usecase.query.PageableResult;
import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionQueryServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionQueryServiceImpl transactionQueryService;

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    public void setUp() {
        transaction1 = Transaction.builder().id("1").build();
        transaction1.setLastUpdateAt(LocalDateTime.of(2023, 1, 1, 12, 0));

        transaction2 = Transaction.builder().id("2").build();
        transaction2.setLastUpdateAt(LocalDateTime.of(2023, 1, 2, 12, 0));
    }

    @Test
    public void getTransactionById_TransactionExists_ReturnsTransaction() {
        when(transactionRepository.findById("1")).thenReturn(Optional.of(transaction1));

        Transaction result = transactionQueryService.getTransactionById("1");

        assertEquals(transaction1, result);
    }

    @Test
    public void getTransactionById_TransactionDoesNotExist_ThrowsException() {
        when(transactionRepository.findById("3")).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> transactionQueryService.getTransactionById("3"));
    }

    @Test
    public void queryPageableTransaction_EmptyTransactionList_ReturnsEmptyPageableResult() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        PageableResult<Transaction> result = transactionQueryService.queryPageableTransaction(1, 10);

        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getTotal());
    }

    @Test
    public void queryPageableTransaction_NonEmptyTransactionList_ReturnsCorrectPageableResult() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        PageableResult<Transaction> result = transactionQueryService.queryPageableTransaction(1, 10);

        assertEquals(2, result.getItems().size());
        assertEquals(2, result.getTotal());
    }

    @Test
    public void queryPageableTransaction_TransactionsLessThanPageSize_ReturnsAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        PageableResult<Transaction> result = transactionQueryService.queryPageableTransaction(1, 5);

        assertEquals(2, result.getItems().size());
        assertEquals(2, result.getTotal());
    }

    @Test
    public void queryPageableTransaction_TransactionsExactlyPageSize_ReturnsCorrectPage() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        PageableResult<Transaction> result = transactionQueryService.queryPageableTransaction(1, 2);

        assertEquals(2, result.getItems().size());
        assertEquals(2, result.getTotal());
    }

    @Test
    public void queryPageableTransaction_TransactionsNotMultipleOfPageSize_ReturnsCorrectLastPage() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        PageableResult<Transaction> result = transactionQueryService.queryPageableTransaction(2, 1);

        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getTotal());
    }
}
