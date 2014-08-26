package com.utils.framework.strings;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/7/13
 * Time: 5:43 PM
 */
public class Capitalizer implements CharTransformer{
    @Override
    public char transform(CharSequence string, int index, char ch) {
        if(index == 0){
            return Character.toUpperCase(ch);
        }

        return ch;
    }
}
