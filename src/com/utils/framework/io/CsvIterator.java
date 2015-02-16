package com.utils.framework.io;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by CM on 2/15/2015.
 */
public class CsvIterator implements Iterator<String[]> {
    private static final Pattern CSV_SPLIT_PATTERN = Pattern.compile("\\s*,\\s*");
    private Iterator<String> lineIterator;

    public CsvIterator(Iterator<String> lineIterator) {
        this.lineIterator = lineIterator;
    }

    @Override
    public boolean hasNext() {
        return lineIterator.hasNext();
    }

    @Override
    public String[] next() {
        return CSV_SPLIT_PATTERN.split(lineIterator.next(), -1);
    }

    @Override
    public void remove() {
        lineIterator.remove();
    }

    public static CsvIterator fileIterator(String fileName, String encoding) throws FileNotFoundException {
        FileNonEmptyLineIterator lineIterator = new FileNonEmptyLineIterator(fileName, "UTF-8");
        return new CsvIterator(lineIterator);
    }
}
