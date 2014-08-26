package com.utils.framework.io;

import java.io.ByteArrayOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 10.01.13
 * Time: 0:02
 * To change this template use File | Settings | File Templates.
 */
public class ReusableByteArrayOutputStream extends ByteArrayOutputStream{
    public ReusableByteArrayOutputStream() {

    }

    public ReusableByteArrayOutputStream(int size) {
        super(size);
    }

    @Override
    public synchronized byte[] toByteArray() {
        return buf;
    }
}
