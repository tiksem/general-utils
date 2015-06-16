package com.utils.framework.collections;

/**
 * User: Tikhonenko.S
 * Date: 12.12.13
 * Time: 13:38
 */
public class EmptyStackException extends RuntimeException {
    public EmptyStackException() {
    }

    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(Throwable cause) {
        super(cause);
    }
}
