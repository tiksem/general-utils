package com.dbbest.framework;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 17:05
 */
public final class Primitive {
    public static int compare(int a, int b){
        if(a < b){
            return -1;
        }

        if(b < a){
            return 1;
        }

        return 0;
    }
}
