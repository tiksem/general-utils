package com.utils.framework;

/**
 * Created by CM on 8/30/2015.
 */
public class KeyProviderHashCodeProvider<K, T> implements HashCodeProvider<T> {
    private KeyProvider<K, T> keyProvider;

    public KeyProviderHashCodeProvider(KeyProvider<K, T> keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Override
    public int getHashCodeOf(T object) {
        return keyProvider.getKey(object).hashCode();
    }
}
