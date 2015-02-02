package com.utils.framework.threading;

/**
 * Created by CM on 1/25/2015.
 */
public class Threads {
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
