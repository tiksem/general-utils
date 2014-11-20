package com.utils.framework.google.images;

import com.utils.framework.io.Network;
import com.utils.framework.parsers.json.ExtendedJSONObject;
import com.utils.framework.parsers.json.JsonArrayElementParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 11/20/2014.
 */
public final class GoogleImagesSearcher {
    public static List<GoogleImage> search(String query, Params params) throws IOException {
        String url = "https://ajax.googleapis.com/ajax/services/search/images";
        Map<String, Object> queryArgs = params.toQueryMap();
        queryArgs.put("v", "1.0");
        queryArgs.put("q", query);

        String resultString = Network.executeGetRequest(url, queryArgs);
        try {
            ExtendedJSONObject json = new ExtendedJSONObject(resultString);
            return json.parseJsonArrayFromPath(new JsonArrayElementParser<GoogleImage>() {
                @Override
                public GoogleImage parse(JSONObject jsonObject) throws JSONException {
                    GoogleImage image = new GoogleImage();
                    image.height = jsonObject.getInt("height");
                    image.width = jsonObject.getInt("width");
                    image.url = jsonObject.getString("url");
                    return image;
                }
            }, "responseData", "results");

        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
}
