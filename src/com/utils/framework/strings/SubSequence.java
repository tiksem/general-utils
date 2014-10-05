package com.utils.framework.strings;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 07.07.13
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public class SubSequence extends AbstractCharSequence {
    private CharSequence charSequence;

    private int first;
    private int last;

    public SubSequence(CharSequence charSequence, int first, int last) {
        this.charSequence = charSequence;
        this.first = first;

        int length = charSequence.length();
        if(last < 0 || last >= length){
            this.last = length - 1;
        } else {
            this.last = last;
        }
    }

    public SubSequence(CharSequence charSequence, int first) {
        this(charSequence, first, -1);
    }

    @Override
    public int length() {
        return last - first + 1;
    }

    @Override
    public char charAt(int i) {
        i = i + first;
        return charSequence.charAt(i);
    }

    @Override
    public CharSequence subSequence(int i, int i2) {
        return new SubSequence(charSequence, i + first, i2);
    }
}
