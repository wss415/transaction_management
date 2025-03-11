package com.hsbc.transaction.domain.context.transaction;

import com.hsbc.transaction.common.enums.TransactionTypeEnum;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 交易对象
 *
 * @author ZhMM
 * @since 2025/3/10 20:42
 **/
@Getter
@Setter
@Builder
public class Transaction {

    /**
     * 主键
     */
    private String id;

    /**
     * 交易金额
     */
    private Double amount;

    /**
     * 交易类型 INCOME || EXPENSE
     */
    private TransactionTypeEnum type;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本
     */
    private Long version;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 更新时间
     */
    private LocalDateTime lastUpdateAt;
}
