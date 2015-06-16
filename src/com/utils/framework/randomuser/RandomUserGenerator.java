package com.utils.framework.randomuser;

import com.utils.framework.io.Network;
import com.utils.framework.parsers.json.ExtendedJSONObject;
import com.utils.framework.parsers.json.JsonArrayElementParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 11/20/2014.
 */
public final class RandomUserGenerator {
    public static List<Response> generate(int count) throws IOException {
        if (count <= 0) {
            return new ArrayList<Response>();
        }

        String stringResponse =
                Network.executeRequestGET("http://api.randomuser.me/?results=" + count);
        ExtendedJSONObject json = null;
        try {
            json = new ExtendedJSONObject(stringResponse);
            return json.parseJsonArrayFromPath(new JsonArrayElementParser<Response>() {
                @Override
                public Response parse(JSONObject jsonObject) throws JSONException {
                    JSONObject user = jsonObject.getJSONObject("user");
                    JSONObject name = user.getJSONObject("name");
                    JSONObject picture = user.getJSONObject("picture");

                    Response response = new Response();
                    response.email = user.getString("email");
                    response.username = user.getString("username");
                    response.password = user.getString("password");
                    response.name = name.getString("first");
                    response.lastName = name.getString("last");
                    response.largeAvatar = picture.getString("large");
                    response.smallAvatar = picture.getString("thumbnail");
                    response.mediumAvatar = picture.getString("medium");
                    response.city = user.getJSONObject("location").getString("city");
                    response.gender = Gender.valueOf(user.getString("gender"));

                    return response;
                }
            }, "results");
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
}
