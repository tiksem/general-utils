package com.utils.framework.io;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public final class Network {
    public static final int DEFAULT_CONNECTION_TIMEOUT = 4000;
    public static final int DEFAULT_READ_TIMEOUT = 6000;

    private static int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private static int readTimeout = DEFAULT_READ_TIMEOUT;

    public static int getConnectionTimeout() {
        return connectionTimeout;
    }

    public static void setConnectionTimeout(int connectionTimeout) {
        Network.connectionTimeout = connectionTimeout;
    }

    public static int getReadTimeout() {
        return readTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        Network.readTimeout = readTimeout;
    }

    public static String getUrl(String url, Map<String, Object> params) throws IOException {
        if(params == null || params.isEmpty()){
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(url);

        urlBuilder.append("?");

        for(Map.Entry<String, Object> param : params.entrySet()){
            String key = URLEncoder.encode(param.getKey(),"UTF-8");
            String value = URLEncoder.encode(param.getValue().toString(),"UTF-8");

            urlBuilder.append(key);
            urlBuilder.append("=");
            urlBuilder.append(value);
            urlBuilder.append("&");
        }

        urlBuilder.deleteCharAt(urlBuilder.length() - 1);

        return urlBuilder.toString();
    }

    public static String executeGetRequest(String url, Map<String, Object> params) throws IOException{
        url = getUrl(url, params);
        return executeRequestGET(url);
    }

    public static <T> T executeRequest(HttpClient httpClient, HttpUriRequest request,
                                       Class<T> aClass) throws IOException {
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        if (aClass == String.class) {
            return (T) IOUtilities.toString(inputStream);
        } else {
            return (T) getBytesFromStream(inputStream);
        }
    }

    public static String executeGetRequest(HttpClient httpClient, String url)
            throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpClient, httpGet, String.class);
    }

    public static byte[] executeBinaryGetRequest(HttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpClient, httpGet, byte[].class);
    }

    public static String executeGetRequest(HttpClient httpClient, String url, Map<String, Object> params)
            throws IOException {
        url = getUrl(url, params);
        return executeGetRequest(httpClient, url);
    }

    public static String getUtf8StringFromUrl(String url, Map<String, Object> params) throws IOException {
        url = getUrl(url, params);
        return new String(getBytesFromUrl(url), "utf-8");
    }

    public static String executeRequestGET(String url) throws IOException {
        InputStream inputStream = null;
        try {
            URL urlObject = new URL(url);
            URLConnection connection = urlObject.openConnection();

//            connection.setReadTimeout(readTimeout);
//            connection.setConnectTimeout(connectionTimeout);

            inputStream = connection.getInputStream();
            InputStreamReader responseReader = new InputStreamReader(inputStream);
            BufferedReader readBuffer = new BufferedReader(responseReader);
            StringBuilder resultString = new StringBuilder();
            String line;
            while ((line = readBuffer.readLine()) != null) {
                resultString.append(line);
            }
            readBuffer.close();
            return resultString.toString();
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    public static interface InterruptListener{
        boolean isInterrupted();
    }

    public static final byte[] getBytesFromStream(InputStream inputStream,
                                                  InterruptListener interruptListener,
                                                  ByteArrayOutputStream byteArrayOutputStream) throws IOException
    {
        try {
            byteArrayOutputStream.reset();
            final int maxReadCount = 1024;
            byte[] buf = new byte[maxReadCount];
            int readCount = 0;
            while (true) {
                if (interruptListener != null) {
                    if (interruptListener.isInterrupted()) {
                        throw new InterruptedIOException();
                    }
                }

                readCount = inputStream.read(buf);
                if (readCount < 0) {
                    break;
                }

                byteArrayOutputStream.write(buf, 0, readCount);
            }

            return byteArrayOutputStream.toByteArray();

        } catch (OutOfMemoryError e) {
            throw new InterruptedIOException();
        }
    }

    public static final byte[] getBytesFromStream(InputStream inputStream,
                                                  InterruptListener interruptListener) throws IOException
    {
        return getBytesFromStream(inputStream, interruptListener, new ReusableByteArrayOutputStream());
    }

    public static final byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        return getBytesFromStream(inputStream, null);
    }

    public static final byte[] getBytesFromUrl(String url,
                                               InterruptListener interruptListener,
                                               ByteArrayOutputStream byteArrayOutputStream) throws IOException{
        InputStream inputStream = null;
        try {
            URL urlObject = new URL(url);
            URLConnection connection = urlObject.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());

            if(byteArrayOutputStream != null) {
                return getBytesFromStream(inputStream, interruptListener, byteArrayOutputStream);
            } else {
                return getBytesFromStream(inputStream, interruptListener);
            }

        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    public static final byte[] getBytesFromUrl(String url,
                                               InterruptListener interruptListener) throws IOException
    {
        return getBytesFromUrl(url, interruptListener, null);
    }

    public static final byte[] getBytesFromUrl(String url) throws IOException {
        return getBytesFromUrl(url, null);
    }
}
