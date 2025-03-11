package com.hsbc.transaction.application.usecase.query.transaction.impl;

import com.hsbc.transaction.application.usecase.query.PageableResult;
import com.hsbc.transaction.application.usecase.query.transaction.TransactionQueryService;
import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionRepository;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author ZhMM
 * @since 2025/3/10 22:33
 **/
@Service
@RequiredArgsConstructor
public class TransactionQueryServiceImpl implements TransactionQueryService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction getTransactionById(String id) {
        return transactionRepository.findById(id).orElseThrow(EntityNotExistException::new);
    }

    @Override
    public PageableResult<Transaction> queryPageableTransaction(int pageIndex, int pageSize) {
        Collection<Transaction> all = transactionRepository.findAll();
        List<Transaction> items = all.stream()
            .sorted(Comparator.comparing(Transaction::getLastUpdateAt))
            .skip((long) (pageIndex - 1) * pageSize)
            .limit(pageSize)
            .toList();

        return new PageableResult<>(pageIndex, pageSize, all.size(), items);
    }
}
