package com.utils.framework.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/28/13
 * Time: 6:14 PM
 */
public class ObjectProbabilityRangeMap<T> {
    private List<T> objects;
    private float[] probabilities;
    private ObjectCoefficientProvider<T> coefficientProvider;

    public ObjectProbabilityRangeMap(List<T> objects, ObjectCoefficientProvider<T> coefficientProvider) {
        this.objects = objects;
        this.coefficientProvider = coefficientProvider;
        probabilities = Statistics.createProbabilitiesArray(objects, coefficientProvider);
    }

    public int getObjectIndexByProbabilityPoint(float probability) {
        int index = Arrays.binarySearch(probabilities, probability);
        if (index < 0) {
            index = -index - 1;
        }

        return index;
    }

    public T getObjectByProbabilityPoint(float probability) {
        int index = getObjectIndexByProbabilityPoint(probability);
        return objects.get(index);
    }

    public T getRandomObject() {
        float probabilityPoint = (float) Math.random();
        return getObjectByProbabilityPoint(probabilityPoint);
    }

    public int getRandomObjectIndex() {
        float probabilityPoint = (float) Math.random();
        return getObjectIndexByProbabilityPoint(probabilityPoint);
    }

    public List<T> generateRandomObjects(int count) {
        List<T> result = new ArrayList<T>(count);
        for (int i = 0; i < count; i++) {
            T randomObject = getRandomObject();
            result.add(randomObject);
        }

        return result;
    }
}
