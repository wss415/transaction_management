package com.hsbc.transaction.application.usecase.command.transaction.impl;

import com.hsbc.transaction.application.usecase.command.transaction.TransactionCommandService;
import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;
import com.hsbc.transaction.domain.context.transaction.TransactionFactory;
import com.hsbc.transaction.domain.context.transaction.TransactionRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ZhMM
 * @since 2025/3/10 22:13
 **/
@Service
@RequiredArgsConstructor
public class TransactionCommandServiceImpl implements TransactionCommandService {

    private final TransactionFactory transactionFactory;
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(TransactionCreateCommand command) {
        Transaction transaction = transactionFactory.create(command);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public void deleteTransactionById(String id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction updateTransactionById(String id, TransactionCreateCommand command) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(EntityNotExistException::new);
        transaction.setAmount(command.getAmount());
        transaction.setType(TransactionTypeEnum.parseByCode(command.getType()));
        transaction.setDescription(command.getDescription());
        transaction.setLastUpdateAt(LocalDateTime.now());
        transactionRepository.update(transaction);
        return transaction;
    }
}
