package com.utils.framework.google.places;

import com.utils.framework.io.Network;
import com.utils.framework.parsers.json.ExtendedJSONObject;
import com.utils.framework.parsers.json.JsonArrayElementParser;
import com.utils.framework.parsers.json.JsonCollections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 12/1/2014.
 */
public class GooglePlacesSearcher {
    private static final String SEARCH_BY_ID_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String AUTO_COMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";

    private String apiKey;
    private Language language = Language.en;

    public GooglePlacesSearcher(String apiKey) {
        this.apiKey = apiKey;
    }

    private Map<String, Object> getArgs() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("key", apiKey);
        args.put("language", language);

        return args;
    }

    private boolean hasCityType(JSONArray types) {
        List<String> strings = JsonCollections.asList(types);
        return strings.contains("locality") || strings.contains("administrative_area_level_3");
    }

    private boolean hasCountryType(JSONArray types) {
        List<String> strings = JsonCollections.asList(types);
        return strings.contains("country");
    }

    public City getCityByPlaceId(String placeId) throws IOException, PlaceIsNotCityException {
        Map<String, Object> args = getArgs();
        args.put("placeid", placeId);

        City city = new City();

        String response = Network.getUtf8StringFromUrl(SEARCH_BY_ID_URL, args);
        try {
            ExtendedJSONObject jsonObject = new ExtendedJSONObject(response);
            JSONObject result = jsonObject.getJsonObjectFromPath("result");
            city.name = result.getString("name");

            if (!hasCityType(result.getJSONArray("types"))) {
                throw new PlaceIsNotCityException(city.name, placeId);
            }

            List<JSONObject> addresses = JsonCollections.asList(
                    jsonObject.getJsonArrayFromPath("result", "address_components"));
            for(JSONObject address : addresses){
                JSONArray types = address.getJSONArray("types");
                if(hasCountryType(types)){
                    city.country = address.getString("long_name");
                    city.countryCode = address.getString("short_name");
                }
            }

            return city;

        } catch (JSONException e) {
            return null;
        }
    }

    public List<AutoCompleteResult> performAutoCompleteCitiesSearch(String query) throws IOException {
        Map<String, Object> args = getArgs();
        args.put("input", query);
        args.put("types", "(cities)");
        String response = Network.getUtf8StringFromUrl(AUTO_COMPLETE_URL, args);
        try {
            ExtendedJSONObject jsonObject = new ExtendedJSONObject(response);
            return jsonObject.parseJsonArrayFromPath(new JsonArrayElementParser<AutoCompleteResult>() {
                @Override
                public AutoCompleteResult parse(JSONObject jsonObject) throws JSONException {
                    AutoCompleteResult result = new AutoCompleteResult();
                    result.placeId = jsonObject.getString("place_id");
                    result.value = jsonObject.getString("description");

                    JSONArray terms = jsonObject.getJSONArray("terms");

                    result.country = terms.getJSONObject(terms.length() - 1).getString("value");
                    result.city = terms.getJSONObject(0).getString("value");

                    return result;
                }
            }, "predictions");

        } catch (JSONException e) {
            return new ArrayList<AutoCompleteResult>();
        }
    }
}
