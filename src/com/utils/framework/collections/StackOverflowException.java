package com.utils.framework.collections;

/**
 * User: Tikhonenko.S
 * Date: 12.12.13
 * Time: 13:35
 */
public class StackOverflowException extends RuntimeException{
    public StackOverflowException() {
    }

    public StackOverflowException(String message) {
        super(message);
    }

    public StackOverflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public StackOverflowException(Throwable cause) {
        super(cause);
    }
}
