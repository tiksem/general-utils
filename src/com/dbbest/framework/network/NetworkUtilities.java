package com.dbbest.framework.network;

import com.dbbest.framework.io.IOUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Tikhonenko.S on 24.09.13.
 */
public final class NetworkUtilities {
    public static URLConnection openConnection(String url) throws IOException {
        URL urlObject = new URL(url);
        return urlObject.openConnection();
    }

    public static String getStringResponseFromPostRequest(String url, byte[] body) throws IOException {
        InputStream inputStream = executePostRequest(url, body);
        return IOUtilities.toString(inputStream);
    }

    public static String getStringResponseFromPostRequest(String url, String body) throws IOException {
        InputStream inputStream = executePostRequest(url, body);
        return IOUtilities.toString(inputStream);
    }

    public static InputStream executePostRequest(String url, byte[] body)
            throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)openConnection(url);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");

        OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(body);

        return urlConnection.getInputStream();
    }

    public static InputStream executePostRequest(String url, String body)
            throws IOException {
        byte[] bytes = body.getBytes();
        return executePostRequest(url, bytes);
    }
}
