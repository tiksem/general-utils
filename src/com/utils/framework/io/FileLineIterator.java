package com.utils.framework.io;

import com.utils.framework.collections.iterator.AbstractIterator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CM on 2/15/2015.
 */
public class FileLineIterator extends AbstractIterator<String> {
    private final InputStreamLineIterator inputStreamLineIterator;
    private final FileInputStream inputStream;

    public FileLineIterator(String fileName, String encoding) throws FileNotFoundException {
        inputStream = new FileInputStream(fileName);
        inputStreamLineIterator = createInputStreamLineIterator(inputStream, encoding);
    }

    protected InputStreamLineIterator createInputStreamLineIterator(InputStream inputStream, String encoding) {
        return new InputStreamLineIterator(inputStream, encoding);
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = inputStreamLineIterator.hasNext();
        if (!hasNext) {
            try {
                inputStream.close();
            } catch (IOException e) {
                inputStreamLineIterator.onIOError(e);
            }
        }

        return hasNext;
    }

    @Override
    public String next() {
        return inputStreamLineIterator.next();
    }

    public IOExceptionListener getIoExceptionListener() {
        return inputStreamLineIterator.getIoExceptionListener();
    }

    public void setIoExceptionListener(IOExceptionListener ioExceptionListener) {
        inputStreamLineIterator.setIoExceptionListener(ioExceptionListener);
    }
}
