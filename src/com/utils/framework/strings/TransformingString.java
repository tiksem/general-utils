package com.utils.framework.strings;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 3/7/13
 * Time: 5:34 PM
 */
public class TransformingString implements CharSequence{
    private CharSequence string;
    private CharTransformer charTransformer;

    public TransformingString(CharSequence string) {
        this.string = string;
    }

    public TransformingString(CharSequence string, CharTransformer charTransformer) {
        this.string = string;
        this.charTransformer = charTransformer;
    }

    public CharTransformer getCharTransformer() {
        return charTransformer;
    }

    public void setCharTransformer(CharTransformer charTransformer) {
        this.charTransformer = charTransformer;
    }

    public CharSequence getString() {
        return string;
    }

    public void setString(CharSequence string) {
        if(string == null){
            throw new NullPointerException();
        }

        this.string = string;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int i) {
        char ch = string.charAt(i);
        return charTransformer.transform(string, i, ch);
    }

    @Override
    public CharSequence subSequence(int i, int i2) {
        return new SubSequence(this, i, i2);
    }

    @Override
    public String toString() {
        return string.toString();
    }

    @Override
    public boolean equals(Object o) {
        return string.equals(o);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
