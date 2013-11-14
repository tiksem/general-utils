package com.dbbest.framework.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Tikhonenko.S on 24.09.13.
 */
public final class IOUtilities {
    private static final int BUFFER_SIZE = 1024;

    public static String toString(Reader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        char[] buffer = new char[1024];
        int n;

        while ( ( n = reader.read(buffer)) != -1 ) {
            content.append(buffer,0,n);
        }

        return content.toString();
    }

    public static Reader readerFromInputStream(InputStream inputStream, String encoding){
        try {
            return new BufferedReader(new InputStreamReader(inputStream, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Reader readerFromInputStream(InputStream inputStream){
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static String toString(InputStream inputStream, String encoding) throws IOException {
        Reader reader = readerFromInputStream(inputStream, encoding);
        return toString(reader);
    }

    public static String toString(InputStream inputStream) throws IOException {
        Reader reader = readerFromInputStream(inputStream);
        return toString(reader);
    }
}
