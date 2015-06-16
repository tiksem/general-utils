package com.utils.framework;

import java.util.Random;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 16:29
 */
public class MathUtils {
    private static Random rand;

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

    public static long sum(int[] array) {
        long sum = 0;

        for (int item : array) {
            sum += item;
        }

        return sum;
    }

    public static int getAverage(int[] array) {
        long sum = sum(array);
        return (int) Math.round((double) sum / (double) array.length);
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        if (rand == null) {
            rand = new Random();
        }

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
