package com.hsbc.transaction.domain.context.transaction;

import java.util.Collection;
import java.util.Optional;

/**
 * 交易对象仓储
 *
 * @author ZhMM
 * @since 2025/3/10 20:52
 **/
public interface TransactionRepository {

    Optional<Transaction> findById(String id);

    void save(Transaction transaction);

    void update(Transaction transaction);

    void deleteById(String id);

    Collection<Transaction> findAll();
}
