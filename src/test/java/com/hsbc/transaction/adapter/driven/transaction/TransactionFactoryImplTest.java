package com.hsbc.transaction.adapter.driven.transaction;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.hsbc.transaction.adapter.driven.IdGenerator;
import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import com.hsbc.transaction.common.exceptions.EnumNotParsedException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionFactoryImplTest {

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private TransactionFactoryImpl transactionFactory;

    @BeforeEach
    public void setUp() {
        // 如果需要，可以在这里进行任何通用的设置
    }

    @Test
    public void create_ValidCommand_TransactionCreated() {
        // 准备
        String transactionId = "T12345";
        double amount = 100.0;
        String typeCode = "INCOME";
        String description = "Test transaction";

        when(idGenerator.generate("T")).thenReturn(transactionId);

        TransactionCreateCommand command = new TransactionCreateCommand(amount, typeCode, description);

        // 执行
        Transaction transaction = transactionFactory.create(command);

        // 验证
        assertNotNull(transaction);
        assertEquals(transactionId, transaction.getId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(TransactionTypeEnum.INCOME, transaction.getType());
        assertEquals(description, transaction.getDescription());
        assertEquals(1L, transaction.getVersion());
        assertNotNull(transaction.getCreateAt());
        assertNotNull(transaction.getLastUpdateAt());
    }

    @Test
    public void create_MinAmount_TransactionCreated() {
        // 准备
        String transactionId = "T12345";
        double amount = 100.0;
        String typeCode = "INCOME";
        String description = "Minimum transaction";

        when(idGenerator.generate("T")).thenReturn(transactionId);

        TransactionCreateCommand command = new TransactionCreateCommand(amount, typeCode, description);

        // 执行
        Transaction transaction = transactionFactory.create(command);

        // 验证
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(TransactionTypeEnum.INCOME, transaction.getType());
    }

    @Test
    public void create_MaxAmount_TransactionCreated() {
        // 准备
        String transactionId = "T12345";
        double amount = 999999999.99;
        String typeCode = "EXPENSE";
        String description = "Maximum transaction";

        when(idGenerator.generate("T")).thenReturn(transactionId);

        TransactionCreateCommand command = new TransactionCreateCommand(amount, typeCode, description);

        // 执行
        Transaction transaction = transactionFactory.create(command);

        // 验证
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertEquals(TransactionTypeEnum.EXPENSE, transaction.getType());
    }

    @Test
    public void create_InvalidTypeCode_ExceptionThrown() {
        // 准备
        double amount = 100.0;
        String invalidTypeCode = "INVALID";
        String description = "Invalid type transaction";

        TransactionCreateCommand command = new TransactionCreateCommand(amount, invalidTypeCode, description);

        // 执行并验证
        assertThrows(EnumNotParsedException.class, () -> transactionFactory.create(command));
    }
}
