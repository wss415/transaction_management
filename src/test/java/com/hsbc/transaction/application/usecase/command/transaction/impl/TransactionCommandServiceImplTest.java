package com.hsbc.transaction.application.usecase.command.transaction.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import com.hsbc.transaction.domain.context.transaction.TransactionFactory;
import com.hsbc.transaction.domain.context.transaction.TransactionRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionCommandServiceImplTest {

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionCommandServiceImpl transactionCommandService;

    private TransactionCreateCommand transactionCreateCommand;
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transactionCreateCommand = new TransactionCreateCommand(100.0, "INCOME", "Test Transaction");

        transaction = Transaction.builder()
            .id("123")
            .amount(50.0)
            .type(TransactionTypeEnum.EXPENSE)
            .description("Test Description")
            .createAt(LocalDateTime.now())
            .lastUpdateAt(LocalDateTime.now())
            .version(1L)
            .build();
    }

    @Test
    public void createTransaction_ValidCommand_TransactionCreated() {
        when(transactionFactory.create(transactionCreateCommand)).thenReturn(transaction);
        doNothing().when(transactionRepository).save(any(Transaction.class));

        Transaction createdTransaction = transactionCommandService.createTransaction(transactionCreateCommand);

        assertEquals(transaction, createdTransaction);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void deleteTransactionById_ValidId_TransactionDeleted() {
        doNothing().when(transactionRepository).deleteById("123");

        transactionCommandService.deleteTransactionById("123");

        verify(transactionRepository, times(1)).deleteById("123");
    }

    @Test
    public void updateTransactionById_TransactionExists_TransactionUpdated() {
        when(transactionRepository.findById("123")).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).update(any(Transaction.class));

        Transaction updatedTransaction =
            transactionCommandService.updateTransactionById("123", transactionCreateCommand);

        assertEquals(100.0, updatedTransaction.getAmount());
        assertEquals(TransactionTypeEnum.INCOME, updatedTransaction.getType());
        assertEquals("Test Transaction", updatedTransaction.getDescription());
        verify(transactionRepository, times(1)).update(any(Transaction.class));
    }

    @Test
    public void updateTransactionById_TransactionDoesNotExist_EntityNotExistExceptionThrown() {
        when(transactionRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(EntityNotExistException.class, () -> {
            transactionCommandService.updateTransactionById("123", transactionCreateCommand);
        });

        verify(transactionRepository, never()).update(any(Transaction.class));
    }
}
