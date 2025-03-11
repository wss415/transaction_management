package com.hsbc.transaction.common.enums;

import com.hsbc.transaction.common.exceptions.EnumNotParsedException;
import lombok.Getter;

/**
 * @author ZhMM
 * @since 2025/3/10 21:41
 **/
@Getter
public enum TransactionTypeEnum {

    INCOME("INCOME", "INCOME"),
    EXPENSE("EXPENSE", "EXPENSE");

    private final String code;
    private final String name;

    TransactionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TransactionTypeEnum parseByCode(String code) {
        for (TransactionTypeEnum enumItem : TransactionTypeEnum.values()) {
            if (enumItem.getCode().equals(code)) {
                return enumItem;
            }
        }
        throw new EnumNotParsedException("can't parse transaction type with:" + code);
    }
}
