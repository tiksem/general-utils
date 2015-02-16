package com.utils.framework.io;

import com.utils.framework.collections.iterator.AbstractIterator;

import java.io.*;
import java.util.NoSuchElementException;

/**
 * Created by CM on 2/15/2015.
 */
public class InputStreamLineIterator extends AbstractIterator<String> {
    private BufferedReader reader;
    private String line;
    private IOExceptionListener ioExceptionListener;
    private boolean isEOF = false;

    public InputStreamLineIterator(InputStream inputStream, String encoding) {
        reader = IOUtilities.getBufferedReader(inputStream, encoding);
    }

    public IOExceptionListener getIoExceptionListener() {
        return ioExceptionListener;
    }

    public void setIoExceptionListener(IOExceptionListener ioExceptionListener) {
        this.ioExceptionListener = ioExceptionListener;
    }

    final void onIOError(IOException e) {
        if(ioExceptionListener != null){
            ioExceptionListener.onIOError(e);
        } else {
            throw new RuntimeException(e);
        }
    }

    protected boolean acceptLine(String line) {
        return true;
    }

    private void readLineIfNeed() {
        if(line == null){
            readLine();
        }
    }

    private void readLine(){
        try {
            while (true) {
                line = reader.readLine();
                if(line == null || acceptLine(line)){
                    break;
                }
            }
            isEOF = line == null;
        } catch (IOException e) {
            onIOError(e);
        }
    }
    
    @Override
    public String next() {
        if(!hasNext()){
            throw new NoSuchElementException("next was called after eof");
        }

        String result = line;
        line = null;
        return result;
    }

    @Override
    public boolean hasNext() {
        readLineIfNeed();
        return !isEOF;
    }
}
