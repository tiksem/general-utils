package com.utils.framework.parsers.json;

import com.utils.framework.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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

    public static Map<String, List<String>> asStringListMap(final JSONObject jsonObject) {
        final Map<String, List<String>> result = new LinkedHashMap<String, List<String>>();
        for(final String key : CollectionUtils.asIterable((Iterator<String>)jsonObject.keys())){
            try {
                result.put(key, new AbstractList<String>() {
                    JSONArray array = jsonObject.getJSONArray(key);

                    @Override
                    public String get(int index) {
                        try {
                            return array.getString(index);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public int size() {
                        return array.length();
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}
