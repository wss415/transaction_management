package com.hsbc.transaction.application.usecase.query.transaction;

import com.hsbc.transaction.application.usecase.query.PageableResult;
import com.hsbc.transaction.domain.context.transaction.Transaction;

/**
 * @author ZhMM
 * @since 2025/3/10 22:12
 **/
public interface TransactionQueryService {

    Transaction getTransactionById(String id);

    PageableResult<Transaction> queryPageableTransaction(int pageIndex, int pageSize);
}
