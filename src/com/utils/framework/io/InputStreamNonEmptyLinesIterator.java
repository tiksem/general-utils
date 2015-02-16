package com.utils.framework.io;

import com.utils.framework.strings.Strings;

import java.io.InputStream;

/**
 * Created by CM on 2/15/2015.
 */
public class InputStreamNonEmptyLinesIterator extends InputStreamLineIterator {
    public InputStreamNonEmptyLinesIterator(InputStream inputStream, String encoding) {
        super(inputStream, encoding);
    }

    @Override
    protected boolean acceptLine(String line) {
        return !Strings.hasOnlySpaceCharacters(line);
    }
}
