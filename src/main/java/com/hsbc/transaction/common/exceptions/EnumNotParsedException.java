package com.hsbc.transaction.common.exceptions;

/**
 * @author ZhMM
 * @since 2025/3/10 21:50
 **/
public class EnumNotParsedException extends RuntimeException {

    public EnumNotParsedException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
