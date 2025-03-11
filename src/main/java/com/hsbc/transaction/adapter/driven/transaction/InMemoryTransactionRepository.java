package com.hsbc.transaction.adapter.driven.transaction;

import com.hsbc.transaction.common.exceptions.EntityNotExistException;
import com.hsbc.transaction.domain.context.transaction.Transaction;
import com.hsbc.transaction.domain.context.transaction.TransactionRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * @author ZhMM
 * @since 2025/3/10 20:59
 **/
@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    private final Map<String, Transaction> indexStore = new ConcurrentHashMap<>(1000);

    @Override
    public Optional<Transaction> findById(String id) {
        Transaction transaction = indexStore.get(id);
        return Optional.ofNullable(transaction);
    }

    @Override
    public void save(Transaction transaction) {
        indexStore.put(transaction.getId(), transaction);
    }

    @Override
    public void update(Transaction transaction) {
        Transaction transactionInStore = findById(transaction.getId())
            .orElseThrow(EntityNotExistException::new);
        //数据版本控制
        Assert.isTrue(Objects.equals(transactionInStore.getVersion(), transaction.getVersion()),
            "data version has been expired, refresh and try again.");
        indexStore.replace(transaction.getId(), transactionInStore, transaction);
    }

    @Override
    public void deleteById(String id) {
        Transaction removed = indexStore.remove(id);
    }

    @Override
    public Collection<Transaction> findAll() {
        return indexStore.values();
    }
}
