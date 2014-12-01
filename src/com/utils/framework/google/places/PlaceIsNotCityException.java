package com.utils.framework.google.places;

import java.io.IOException;

/**
 * Created by CM on 12/1/2014.
 */
public class PlaceIsNotCityException extends IOException {
    public PlaceIsNotCityException(String placeName, String placeId) {
        super("Place " + placeName + "(" + placeId + ") is not a city");
    }
}
