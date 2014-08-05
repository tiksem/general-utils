package com.utils.framework.collections.cache;

/**
 *
 * User: stikhonenko
 * Date: 2/25/13
 * Time: 2:38 PM
 */
public class StringLruCache extends LruCache<String,String> {
    private static final int SIZE_OF_CHAR = 2;
    private static final int EMPTY_STRING_OVERHEAD = 8 + 8 + 8;

    public StringLruCache(int maxSize) {
        super(maxSize);
    }

    public static int sizeOfStrings(String key, String value){
        return SIZE_OF_CHAR * key.length() + SIZE_OF_CHAR * value.length() + EMPTY_STRING_OVERHEAD * 2;
    }

    @Override
    public int sizeOf(String key, String value) {
        return sizeOfStrings(key, value);
    }
}
