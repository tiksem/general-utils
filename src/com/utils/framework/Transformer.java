package com.utils.framework;

/**
 * Created by stykhonenko on 27.10.15.
 */
public interface Transformer<From, To> {
    To get(From from);
}
