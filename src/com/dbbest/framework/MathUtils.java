package com.dbbest.framework;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 16:29
 */
public class MathUtils {
    public static int getGreatestCommonDivisor(int a, int b) {
        if (a == 0)
            return b;

        while (b != 0) {
            if (a > b)
                a = a - b;
            else
                b = b - a;
        }

        return a;
    }

    public static long sum(int[] array){
        long sum = 0;

        for(int item : array){
            sum += item;
        }

        return sum;
    }

    public static int getAverage(int[] array){
        long sum = sum(array);
        return (int) Math.round((double) sum / (double) array.length);
    }
}
