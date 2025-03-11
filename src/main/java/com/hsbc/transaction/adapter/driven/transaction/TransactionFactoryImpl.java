package com.hsbc.transaction.adapter.driven.transaction;

import com.hsbc.transaction.adapter.driven.IdGenerator;
import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import com.hsbc.transaction.domain.context.transaction.TransactionFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author ZhMM
 * @since 2025/3/10 21:22
 **/
@Component
@RequiredArgsConstructor
public class TransactionFactoryImpl implements TransactionFactory {

    private final IdGenerator idGenerator;

    @Override
    public Transaction create(TransactionCreateCommand command) {
        LocalDateTime now = LocalDateTime.now();
        return Transaction.builder()
            .id(idGenerator.generate("T"))
            .amount(command.getAmount())
            .type(TransactionTypeEnum.parseByCode(command.getType()))
            .description(command.getDescription())
            .version(1L)
            .createAt(now)
            .lastUpdateAt(now)
            .build();
    }
}
