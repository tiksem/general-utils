package com.utils.framework.strings;

/**
 * Created by CM on 10/4/2014.
 */
public abstract class AbstractCharSequence implements CharSequence {
    @Override
    public CharSequence subSequence(int start, int end) {
        return Strings.copySubSequence(this, start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof CharSequence) {
            return Strings.charSequenceEquals(this, (CharSequence) o);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new StringBuilder(this).hashCode();
    }

    @Override
    public String toString() {
        return Strings.copyCharSequence(this);
    }
}
