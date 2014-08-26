package com.utils.framework.algorithms;

import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/28/13
 * Time: 6:12 PM
 */
public final class Statistics {
    public static float getObjectProbability(ObjectCoefficientProvider probabilityProvider, Object object){
        float result = probabilityProvider.get(object);
        if(result < 0.0f || result > 1.0f){
            throw new IllegalArgumentException();
        }

        return result;
    }

    public static  <T> float sum(List<T> objects, ObjectCoefficientProvider<T> coefficientProvider){
        float result = 0;
        for(T object : objects){
            result += coefficientProvider.get(object);
        }

        return result;
    }

    public static  <T> float[] createProbabilitiesArray(List<T> objects, ObjectCoefficientProvider<T> coefficientProvider){
        float sum = sum(objects, coefficientProvider);
        float probabilitiesSum = 0;
        float[] result = new float[objects.size()];

        for(int i = 0; i < result.length; i++){
            T object = objects.get(i);
            float coefficient = coefficientProvider.get(object);
            probabilitiesSum += coefficient / sum;
            result[i] = probabilitiesSum;
        }

        return result;
    }
}
