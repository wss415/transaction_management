package com.hsbc.transaction.domain.context.transaction;

/**
 * 交易对象创建工厂
 *
 * @author ZhMM
 * @since 2025/3/10 21:18
 **/
public interface TransactionFactory {

    Transaction create(TransactionCreateCommand command);
}
