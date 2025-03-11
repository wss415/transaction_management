package com.hsbc.transaction.application.usecase.command.transaction;

import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionCreateCommand;

/**
 * @author ZhMM
 * @since 2025/3/10 21:55
 **/
public interface TransactionCommandService {

    Transaction createTransaction(TransactionCreateCommand command);

    void deleteTransactionById(String id);

    Transaction updateTransactionById(String id, TransactionCreateCommand command);
}
