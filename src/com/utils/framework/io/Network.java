package com.utils.framework.io;

import com.utils.framework.ArrayUtils;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Transformer;
import com.utils.framework.strings.Strings;
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
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


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

    public static String getOrderedQueryStringUrl(String url, Map<String, Object> params) throws IOException {
        if (params == null || params.isEmpty()) {
            return url;
        }

        if (!(params instanceof SortedMap)) {
            params = new TreeMap<>(params);
        }

        return getUrl(url, params);
    }

    public static String getUrl(String url, Map<String, Object> params) throws IOException {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder(url);

        int indexOf = url.lastIndexOf('?');
        if (indexOf > 0) {
            urlBuilder.append('&');
        } else {
            urlBuilder.append('?');
        }

        appendQueryString(params, urlBuilder);
        return urlBuilder.toString();
    }

    public static String executeGetRequest(String url, Map<String, Object> params, String encoding) throws IOException {
        url = getUrl(url, params);
        return executeRequestGET(url, encoding);
    }

    public static String executeGetRequest(String url, Map<String, Object> params) throws IOException {
        return executeGetRequest(url, params, null);
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
        return executeRequestGET(url, null);
    }

    public static String executeRequestGET(String url, String encoding) throws IOException {
        InputStream inputStream = null;
        try {
            if (encoding == null) {
                encoding = Charset.defaultCharset().name();
            }

            URL urlObject = new URL(url);
            URLConnection connection = urlObject.openConnection();

            inputStream = connection.getInputStream();
            return IOUtilities.toString(inputStream, encoding);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static interface InterruptListener {
        boolean isInterrupted();
    }

    public static final byte[] getBytesFromStream(InputStream inputStream,
                                                  InterruptListener interruptListener,
                                                  ByteArrayOutputStream byteArrayOutputStream) throws IOException {
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
                                                  InterruptListener interruptListener) throws IOException {
        return getBytesFromStream(inputStream, interruptListener, new ReusableByteArrayOutputStream());
    }

    public static final byte[] getBytesFromStream(InputStream inputStream) throws IOException {
        return getBytesFromStream(inputStream, null);
    }

    public static final byte[] getBytesFromUrl(String url,
                                               InterruptListener interruptListener,
                                               ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        InputStream inputStream = null;
        try {
            URL urlObject = new URL(url);
            URLConnection connection = urlObject.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());

            if (byteArrayOutputStream != null) {
                return getBytesFromStream(inputStream, interruptListener, byteArrayOutputStream);
            } else {
                return getBytesFromStream(inputStream, interruptListener);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static final byte[] getBytesFromUrl(String url,
                                               InterruptListener interruptListener) throws IOException {
        return getBytesFromUrl(url, interruptListener, null);
    }

    public static final byte[] getBytesFromUrl(String url) throws IOException {
        return getBytesFromUrl(url, null);
    }

    public static int ip4StringToInt(String stringIP) {
        byte[] bytes = ArrayUtils.stringsToBytes(stringIP.split("\\.", 4));
        return ArrayUtils.byteArrayToInt(bytes);
    }

    public static String ip4IntToString(int intIP) {
        byte[] bytes = ArrayUtils.intToByteArray(intIP);
        return Strings.join(".", ArrayUtils.bytesToStrings(bytes)).toString();
    }

    public static StringBuilder appendQueryString(Map<String, Object> params, StringBuilder out) {
        Iterator<String> iterator = CollectionUtils.transform(params.entrySet().iterator(),
                new Transformer<Map.Entry<String, Object>, String>() {
                    @Override
                    public String get(Map.Entry<String, Object> param) {
                        try {
                            String key = URLEncoder.encode(param.getKey(), "UTF-8");
                            String value = URLEncoder.encode(param.getValue().toString(), "UTF-8");
                            return key + "=" + value;
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException("UTF-8 is not supported");
                        }
                    }
                });

        Strings.join("&", iterator, out);
        return out;
    }

    public static String toQueryString(Map<String, Object> params) {
        return appendQueryString(params, new StringBuilder()).toString();
    }
}
