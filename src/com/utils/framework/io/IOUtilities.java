package com.utils.framework.io;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by Tikhonenko.S on 24.09.13.
 */
public final class IOUtilities {
    private static final int BUFFER_SIZE = 1024;
    private static final int COPY_BUFFER_SIZE = 32 * 1024; // 32 KB

    public static String toString(Reader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        int n;

        while ((n = reader.read(buffer)) != -1) {
            content.append(buffer, 0, n);
        }

        return content.toString();
    }

    public static Reader bufferedReaderFromInputStream(InputStream inputStream, String encoding) {
        try {
            return new BufferedReader(new InputStreamReader(inputStream, encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Reader bufferedReaderFromInputStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static String toString(InputStream inputStream, String encoding) throws IOException {
        Reader reader = new InputStreamReader(inputStream, encoding);
        return toString(reader);
    }

    public static String toString(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        return toString(reader);
    }

    // write CharSequence
    //-----------------------------------------------------------------------

    /**
     * Writes chars from a <code>CharSequence</code> to a <code>Writer</code>.
     *
     * @param data   the <code>CharSequence</code> to write, null ignored
     * @param output the <code>Writer</code> to write to
     * @throws NullPointerException if output is null
     * @throws java.io.IOException  if an I/O error occurs
     * @since 2.0
     */
    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the default character encoding of the
     * platform.
     * <p>
     * This method uses {@link String#getBytes()}.
     *
     * @param data   the <code>CharSequence</code> to write, null ignored
     * @param output the <code>OutputStream</code> to write to
     * @throws NullPointerException if output is null
     * @throws java.io.IOException  if an I/O error occurs
     * @since 2.0
     */
    public static void write(CharSequence data, OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    /**
     * Writes chars from a <code>CharSequence</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>CharSequence</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws java.io.IOException  if an I/O error occurs
     * @since 2.3
     */
    public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }
    }

    public static InputStream getBufferedInputStream(InputStream stream) {
        if (stream instanceof BufferedInputStream) {
            return stream;
        } else {
            return new BufferedInputStream(stream);
        }
    }

    public static OutputStream getBufferedOutputStream(OutputStream stream) {
        if (stream instanceof BufferedOutputStream) {
            return stream;
        } else {
            return new BufferedOutputStream(stream);
        }
    }

    public static InputStream getBufferedInputStreamFromUrl(String url) throws IOException {
        InputStream stream = Network.getInputStreamFromUrl(url);
        return getBufferedInputStream(stream);
    }

    public static boolean createFile(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            file.delete();
        }
        return file.createNewFile();
    }

    public static FileInputStream getOrCreateFileInputStream(String url) throws IOException {
        createFile(url);
        return new FileInputStream(url);
    }

    public static InputStream getOrCreateFileBufferedInputStream(String url) throws IOException {
        FileInputStream stream = getOrCreateFileInputStream(url);
        return getBufferedInputStream(stream);
    }

    public static FileOutputStream getOrCreateFileOutputStream(String url) throws IOException {
        createFile(url);
        return new FileOutputStream(url);
    }

    public static OutputStream getOrCreateFileBufferedOutputStream(String url) throws IOException {
        FileOutputStream stream = getOrCreateFileOutputStream(url);
        return getBufferedOutputStream(stream);
    }

    public static InputStream getBufferedInputStreamFromUrl(String url, int buffSize) throws IOException {
        InputStream stream = Network.getInputStreamFromUrl(url);
        return new BufferedInputStream(stream, buffSize);
    }

    public static ObjectOutputStream getObjectOutputStreamFromFile(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        return new ObjectOutputStream(fileOutputStream);
    }

    public static ObjectInputStream getObjectInputStreamFromFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        return new ObjectInputStream(fileInputStream);
    }

    public static Object getObjectFromFile(File file) {
        try {
            ObjectInputStream inputStream = getObjectInputStreamFromFile(file);
            return inputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean writeObjectToFile(Object object, File file) {
        try {
            ObjectOutputStream objectOutputStream = getObjectOutputStreamFromFile(file);
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static String readStringFromUrl(String filePath) throws IOException {
        InputStream inputStream = Network.getInputStreamFromUrl(filePath);
        try {
            return toString(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static String readSourceFile(String path) throws IOException {
        InputStream inputStream = IOUtilities.class.getClassLoader().
                getResourceAsStream(path);
        return toString(inputStream);
    }

    public static <T extends Serializable> T deserialize(InputStream inputStream) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    public static InputStream toInputStream(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(object);

            outputStream.flush();
            outputStream.close();

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean removeDirectory(File directory) {
        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                if (entry.isDirectory()) {
                    if (!removeDirectory(entry))
                        return false;
                } else {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return directory.delete();
    }

    public static String getUtf8StringFromStream(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader buffReader = new BufferedReader(inputStreamReader);
        return toString(buffReader);
    }

    public static BufferedReader getBufferedReader(InputStream inputStream, String encoding) {
        InputStreamReader responseReader = null;
        try {
            responseReader = new InputStreamReader(inputStream, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return new BufferedReader(responseReader);
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[COPY_BUFFER_SIZE];
        while (true) {
            int count = is.read(bytes, 0, COPY_BUFFER_SIZE);
            if (count == -1) {
                break;
            }
            os.write(bytes, 0, count);
        }
    }

    public static void copyStream(InputStream is, OutputStream os, int maxProgress, ProgressListener progressListener)
            throws IOException {
        byte[] bytes = new byte[COPY_BUFFER_SIZE];
        while (true) {
            int count = is.read(bytes, 0, COPY_BUFFER_SIZE);
            if (count == -1) {
                break;
            }
            os.write(bytes, 0, count);
            progressListener.onProgressChanged(count, maxProgress);
        }
    }

    public static void copyStream(InputStream is, OutputStream os, ProgressListener progressListener)
            throws IOException {
        copyStream(is, os, -1, progressListener);
    }

    public static void copyFile(String source, String destination) throws IOException {
        copyStream(new FileInputStream(source), new FileOutputStream(destination));
    }

    public static final void closeSilently(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            // Do nothing
        }
    }
}
