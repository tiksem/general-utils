package com.utils.framework.collections.cache;

import com.utils.framework.io.IOUtilities;
import com.utils.framework.naming.FileNameGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 4:09 PM
 */
public class StringLruDiskCache extends BaseLruDiskCache<String, String> {
    private CacheDirectoryPathGenerator cacheDirectoryPathGenerator;

    protected StringLruDiskCache(int maxSize, CacheDirectoryPathGenerator cacheDirectoryPathGenerator,
                                 FileNameGenerator fileNameGenerator) {
        super(maxSize, fileNameGenerator);
        this.cacheDirectoryPathGenerator = cacheDirectoryPathGenerator;
    }

    public StringLruDiskCache(int maxSize, CacheDirectoryPathGenerator cacheDirectoryPathGenerator) {
        super(maxSize);
        this.cacheDirectoryPathGenerator = cacheDirectoryPathGenerator;
    }

    @Override
    protected void writeToStream(OutputStream stream, String value) throws IOException {
        IOUtilities.write(value, stream);
    }

    @Override
    protected String readFromStream(InputStream stream) {
        try {
            return IOUtilities.toString(stream);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected String getCacheDirectory() {
        return cacheDirectoryPathGenerator.getCachingDirectory();
    }
}
