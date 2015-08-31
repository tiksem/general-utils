package com.utils.framework;

/**
 * Created by CM on 8/30/2015.
 */
public class KeyProviderEquals<K, T> implements Equals<T> {
    private KeyProvider<K, T> keyProvider;

    public KeyProviderEquals(KeyProvider<K, T> keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Override
    public boolean equals(T a, T b) {
        K keyA = keyProvider.getKey(a);
        K keyB = keyProvider.getKey(b);
        return keyA.equals(keyB);
    }
}
