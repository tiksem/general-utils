package com.utils.framework;

import java.util.Collection;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 18:23
 */
public class CancelableUtils {
    public static void cancelAllAndClearQueue(Collection<Cancelable> cancelables) {
        for (Cancelable cancelable : cancelables) {
            cancelable.cancel();
        }
        cancelables.clear();
    }
}
