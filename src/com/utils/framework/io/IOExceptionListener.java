package com.utils.framework.io;

import java.io.IOException;

/**
 * Created by CM on 2/15/2015.
 */
public interface IOExceptionListener {
    void onIOError(IOException e);
}
