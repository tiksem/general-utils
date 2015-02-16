package com.utils.framework.io;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by CM on 2/15/2015.
 */
public class FileNonEmptyLineIterator extends FileLineIterator {
    public FileNonEmptyLineIterator(String fileName, String encoding) throws FileNotFoundException {
        super(fileName, encoding);
    }

    @Override
    protected InputStreamLineIterator createInputStreamLineIterator(InputStream inputStream, String encoding) {
        return new InputStreamNonEmptyLinesIterator(inputStream, encoding);
    }
}
