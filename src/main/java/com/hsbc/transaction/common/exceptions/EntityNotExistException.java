package com.hsbc.transaction.common.exceptions;

/**
 * @author ZhMM
 * @since 2025/3/10 22:16
 **/
public class EntityNotExistException extends RuntimeException {

    public EntityNotExistException() {
        super("business entity not found,refresh and try again.");
    }

    public EntityNotExistException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
