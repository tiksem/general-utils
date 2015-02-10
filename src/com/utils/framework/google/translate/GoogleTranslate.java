package com.utils.framework.google.translate;

import com.utils.framework.io.IOUtilities;
import com.utils.framework.io.Network;
import com.utils.framework.threading.Threads;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;

/**
 * Created by CM on 2/8/2015.
 */
public class GoogleTranslate {
    public static String simpleTranslate(String fromLangCode, String toLangCode, String query) throws IOException {
        String url = "https://translate.google.com/translate_a/single?client=t&" +
                "sl=" + fromLangCode +
                "&tl=" + toLangCode +
                "&hl=en&dt=bd&dt=ex&dt=ld&dt=md&dt=qc&dt=rw&dt=rm&" +
                "dt=ss&dt=t&dt=at&ie=UTF-8&oe=UTF-8&prev=bh&ssel=0" +
                "&tsel=0&tk=518850%7C902264&" +
                "q=" + URLEncoder.encode(query, "UTF-8");

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(URI.create(url));
        HttpResponse httpResponse = httpClient.execute(get);
        InputStream content = httpResponse.getEntity().getContent();

        String response = IOUtilities.getUtf8StringFromStream(content);
        int begin = response.indexOf('"') + 1;
        int end = response.indexOf('"', begin);
        return response.substring(begin, end);
    }

    public static String simpleTranslateTryAgainIfFailed(String fromLangCode, String toLangCode, String query)
            throws IOException {
        for (int i = 0; i < 3; i++) {
            try {
                return simpleTranslate(fromLangCode, toLangCode, query);
            } catch (IOException e) {
                if(i == 2 || !e.getMessage().contains("403")){
                    throw e;
                } else {
                    Threads.sleep(1000);
                }
            }
        }

        throw new RuntimeException("WTF?");
    }
}
