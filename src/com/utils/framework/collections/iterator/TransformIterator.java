package com.utils.framework.collections.iterator;

import com.utils.framework.Transformer;

import java.util.Iterator;

/**
 * Created by stykhonenko on 16.11.15.
 */
public class TransformIterator<From, To> extends AbstractIterator<To> {
    private Iterator<From> fromIterator;
    private Transformer<From, To> transformer;

    public TransformIterator(Iterator<From> fromIterator, Transformer<From, To> transformer) {
        this.fromIterator = fromIterator;
        this.transformer = transformer;
    }

    @Override
    public boolean hasNext() {
        return fromIterator.hasNext();
    }

    @Override
    public To next() {
        From next = fromIterator.next();
        return transformer.get(next);
    }
}
