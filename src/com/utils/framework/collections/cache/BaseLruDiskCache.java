package com.utils.framework.collections.cache;

import com.utils.framework.io.IOUtilities;
import com.utils.framework.naming.FileNameGenerator;
import com.utils.framework.naming.Md5FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 3:14 PM
 */
public abstract class BaseLruDiskCache<K, V> implements Cache<K, V> {
    private FileNameGenerator fileNameGenerator;
    private LruCache<String, String> cache;

    private String getFileName(String key) {
        return getCacheDirectory() + File.separatorChar + fileNameGenerator.generate(key);
    }

    protected BaseLruDiskCache(int maxSize, FileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
        cache = new LruCache<String, String>(maxSize) {
            @Override
            public int sizeOf(String key, String fileName) {
                File file = new File(fileName);
                return (int) file.length();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, String fileName, String newValue) {
                new File(fileName).delete();
            }
        };
    }

    protected BaseLruDiskCache(int maxSize) {
        this(maxSize, new Md5FileNameGenerator());
    }

    @Override
    public V get(K key) {
        String fileName = cache.get(key.toString());
        if (fileName != null) {
            try {
                InputStream stream = IOUtilities.getBufferedInputStreamFromUrl(fileName);
                V value = readFromStream(stream);
                if (value != null) {
                    return value;
                }

            } catch (IOException e) {

            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        V old = null;

        if (cache.get(key.toString()) != null) {
            old = get(key);
        }

        String fileName = getFileName(key.toString());

        try {
            OutputStream stream = IOUtilities.getOrCreateFileBufferedOutputStream(fileName);
            writeToStream(stream, value);
            cache.put(key.toString(), fileName);
        } catch (IOException e) {

        }

        return old;
    }

    @Override
    public boolean contains(K key) {
        return cache.contains(key.toString());
    }

    protected abstract void writeToStream(OutputStream stream, V value) throws IOException;

    protected abstract V readFromStream(InputStream stream);

    protected abstract String getCacheDirectory();
}
