package com.utils.framework;

/**
 * Created by CM on 7/9/2015.
 */
public abstract class OneTimeRunnable implements Runnable {
    private boolean executed = false;
    protected abstract void doAction();

    @Override
    public void run() {
        if (!executed) {
            doAction();
            executed = true;
        }
    }
}
