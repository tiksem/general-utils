package com.utils.framework.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.List;

/**
 * Created by CM on 12/1/2014.
 */
public class JsonCollections {
    public static <T> List<T> asList(final JSONArray jsonArray) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                try {
                    return (T) jsonArray.get(index);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public int size() {
                return jsonArray.length();
            }
        };
    }
}
