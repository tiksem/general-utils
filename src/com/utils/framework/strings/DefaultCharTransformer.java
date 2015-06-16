package com.utils.framework.strings;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/7/13
 * Time: 5:38 PM
 */
public class DefaultCharTransformer implements CharTransformer {
    @Override
    public char transform(CharSequence string, int index, char ch) {
        return ch;
    }
}
