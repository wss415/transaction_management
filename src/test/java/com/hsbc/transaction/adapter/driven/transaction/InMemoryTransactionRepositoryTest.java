package com.hsbc.transaction.adapter.driven.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository transactionRepository;

    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        transactionRepository = new InMemoryTransactionRepository();
        transaction1 = Transaction.builder()
            .id("1").amount(100.0).type(TransactionTypeEnum.INCOME).description("Transaction 1").version(10L).build();
        transaction2 = Transaction.builder()
            .id("2").amount(200.0).type(TransactionTypeEnum.EXPENSE).description("Transaction 2").version(20L).build();
    }

    @Test
    void findById_TransactionExists_ReturnsTransaction() {
        transactionRepository.save(transaction1);
        Optional<Transaction> result = transactionRepository.findById("1");
        assertTrue(result.isPresent(), "Expected a present optional");
        assertEquals(transaction1, result.get(), "Expected the same transaction object");
    }

    @Test
    void findById_TransactionDoesNotExist_ReturnsEmptyOptional() {
        Optional<Transaction> result = transactionRepository.findById("1");
        assertFalse(result.isPresent(), "Expected an empty optional");
    }

    @Test
    void save_Transaction_AddsToStore() {
        transactionRepository.save(transaction1);
        Optional<Transaction> result = transactionRepository.findById("1");
        assertTrue(result.isPresent(), "Expected a present optional after saving");
        assertEquals(transaction1, result.get(), "Expected the saved transaction to be retrieved");
    }

    @Test
    void update_TransactionDoesNotExist_ThrowsEntityNotExistException() {
        Exception exception = assertThrows(EntityNotExistException.class, () -> {
            transactionRepository.update(transaction1);
        });
        assertNotNull(exception, "Expected EntityNotExistException to be thrown");
    }

    @Test
    void update_VersionMismatch_ThrowsIllegalArgumentException() {
        transactionRepository.save(transaction1);
        Transaction updatedTransaction = Transaction.builder()
            .id("1").amount(100.0).type(TransactionTypeEnum.INCOME).description("Transaction 1").version(1L).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionRepository.update(updatedTransaction);
        });
        assertNotNull(exception, "Expected IllegalArgumentException due to version mismatch");
    }

    @Test
    void update_VersionMatches_UpdatesTransaction() {
        transactionRepository.save(transaction1);
        Transaction updatedTransaction = Transaction.builder()
            .id("1").amount(100.0).type(TransactionTypeEnum.INCOME).description("Transaction 1").version(10L).build();
        transactionRepository.update(updatedTransaction);

        Optional<Transaction> result = transactionRepository.findById("1");
        assertTrue(result.isPresent(), "Expected a present optional after update");
        assertEquals(updatedTransaction, result.get(), "Expected the updated transaction to be retrieved");
    }

    @Test
    void deleteById_TransactionExists_RemovesTransaction() {
        transactionRepository.save(transaction1);
        transactionRepository.deleteById("1");

        Optional<Transaction> result = transactionRepository.findById("1");
        assertFalse(result.isPresent(), "Expected an empty optional after deletion");
    }

    @Test
    void deleteById_TransactionDoesNotExist_NoException() {
        // No assertion needed as no exception is expected
        transactionRepository.deleteById("1");
    }

    @Test
    void findAll_ReturnsAllTransactions() {
        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);

        Collection<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size(), "Expected two transactions in the collection");
        assertTrue(transactions.contains(transaction1), "Expected transaction1 to be in the collection");
        assertTrue(transactions.contains(transaction2), "Expected transaction2 to be in the collection");
    }
}
