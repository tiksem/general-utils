package com.utils.framework.system;

/**
 * Created by Tikhonenko.S on 04.10.13.
 */
public class OutOfMemoryException extends Exception{
    public OutOfMemoryException(String message, OutOfMemoryError cause) {
        super(message, cause);
    }

    public OutOfMemoryException(OutOfMemoryError cause) {
        super(cause);
    }
}
