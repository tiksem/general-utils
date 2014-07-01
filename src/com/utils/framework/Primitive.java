package com.utils.framework;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 17:05
 */
public final class Primitive {
    private static final float EQUALS_DELTA = 0.0005f;

    public static int compare(int a, int b){
        if(a < b){
            return -1;
        }

        if(b < a){
            return 1;
        }

        return 0;
    }

    public static boolean floatEquals(float a, float b) {
        return Math.abs(a - b) <= EQUALS_DELTA;
    }
}
