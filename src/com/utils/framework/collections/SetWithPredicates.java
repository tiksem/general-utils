package com.utils.framework.collections;

import com.utils.framework.*;

import java.util.*;

public class SetWithPredicates<T> extends AbstractSet<T> {
    private HashCodeProvider hashCodeProvider;
    private Equals equalsPredicate;
    private Set<Entry<T>> set;

    public class Entry<T> {
        T object;

        private Entry(T object) {
            this.object = object;
        }

        @Override
        public boolean equals(Object o) {
            Entry<T> entry = (Entry<T>) o;
            o = entry.object;
            return equalsPredicate.equals(object, o);
        }

        @Override
        public int hashCode() {
            return hashCodeProvider.getHashCodeOf(object);
        }
    }

    private void initEqualsPredicate(Equals equalsPredicate) {
        if (equalsPredicate == null) {
            equalsPredicate = new DefaultEquals();
        }

        this.equalsPredicate = equalsPredicate;
    }

    private void initHashCodeProvider(HashCodeProvider hashCodeProvider) {
        if (hashCodeProvider == null) {
            hashCodeProvider = new DefaultHashCodeProvider();
        }

        this.hashCodeProvider = hashCodeProvider;
    }

    private void initSet(Set set) {
        if (set == null) {
            set = new HashSet();
        } else if (!set.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.set = set;
    }

    public SetWithPredicates(Equals equalsPredicate) {
        this(null, equalsPredicate, null);
    }

    public SetWithPredicates(Equals equalsPredicate, HashCodeProvider hashCodeProvider) {
        this(null, equalsPredicate, hashCodeProvider);
    }

    public SetWithPredicates(Set set) {
        this(set, null);
    }

    public SetWithPredicates(Set set, Equals<T> equalsPredicate) {
        this(set, equalsPredicate, null);
    }

    public SetWithPredicates(Set set, Equals equalsPredicate, HashCodeProvider hashCodeProvider) {
        initSet(set);
        initEqualsPredicate(equalsPredicate);
        initHashCodeProvider(hashCodeProvider);
    }

    @Override
    public boolean add(T t) {
        if (t != null) {
            return set.add(new Entry<T>(t));
        } else {
            throw new NullPointerException("null values are not allowed");
        }
    }

    public SetWithPredicates() {
        this(null, null, null);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(new Entry<T>((T) o));
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Iterator<Entry<T>> iterator = set.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next().object;
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    public Equals<T> getEqualsPredicate() {
        return equalsPredicate;
    }

    public HashCodeProvider<T> getHashCodeProvider() {
        return hashCodeProvider;
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(new Entry<T>((T) o));
    }

    @Override
    public int size() {
        return set.size();
    }

    public static <T> SetWithPredicates<T> fromKeyProvider(KeyProvider<? extends Object, T> keyProvider) {
        return new SetWithPredicates<>(new KeyProviderEquals<>(keyProvider),
                new KeyProviderHashCodeProvider<>(keyProvider));
    }
}
