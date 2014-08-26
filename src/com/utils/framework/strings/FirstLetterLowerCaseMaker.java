package com.utils.framework.strings;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 06.07.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public class FirstLetterLowerCaseMaker implements CharTransformer{
    @Override
    public char transform(CharSequence string, int index, char ch) {
        if(index == 0){
            return Character.toLowerCase(ch);
        }

        return ch;
    }
}
