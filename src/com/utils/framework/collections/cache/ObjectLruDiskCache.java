package com.utils.framework.collections.cache;

import com.utils.framework.naming.FileNameGenerator;

import java.io.*;

/**
 * User: stikhonenko
 * Date: 3/13/13
 * Time: 1:06 PM
 */
public class ObjectLruDiskCache<K, V> extends BaseLruDiskCache<K, V> {
    private CacheDirectoryPathGenerator cacheDirectoryPathGenerator;

    public ObjectLruDiskCache(int maxSize, CacheDirectoryPathGenerator cacheDirectoryPathGenerator) {
        super(maxSize);
        this.cacheDirectoryPathGenerator = cacheDirectoryPathGenerator;
    }

    public ObjectLruDiskCache(int maxSize, FileNameGenerator fileNameGenerator,
                              CacheDirectoryPathGenerator cacheDirectoryPathGenerator) {
        super(maxSize, fileNameGenerator);
        this.cacheDirectoryPathGenerator = cacheDirectoryPathGenerator;
    }

    @Override
    protected void writeToStream(OutputStream stream, V value) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(value);
    }

    @Override
    protected V readFromStream(InputStream stream) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(stream);
            return (V) objectInputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected String getCacheDirectory() {
        return cacheDirectoryPathGenerator.getCachingDirectory();
    }
}
