package com.utils.framework.strings;

import com.utils.framework.Reflection;
import com.utils.framework.strings.Capitalizer;
import com.utils.framework.strings.FirstLetterLowerCaseMaker;
import com.utils.framework.strings.TransformingString;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public class Strings {
    public static <T extends CharSequence> int length(List<T> sequences){
        int size = 0;
        for(CharSequence charSequence : sequences){
            if (charSequence != null) {
                size += charSequence.length();
            }
        }
        return size;
    }

    public static int length(CharSequence[] sequences){
        return length(Arrays.asList(sequences));
    }

    public static <T extends CharSequence> void join(CharSequence separator, List<T> parts,
                                                              StringBuilder out){
        int partsSize = parts.size();
        if(partsSize == 0){
            return;
        }

        int size = length(parts) + separator.length() * partsSize - 1;
        int index = 0;
        for(CharSequence part : parts){
            out.append(part);
            index++;
            if(index != partsSize){
                out.append(separator);
            }
        }
    }

    public static <T extends CharSequence> StringBuilder join(CharSequence separator, List<T> parts){
        StringBuilder result = new StringBuilder();
        join(separator, parts, result);
        return result;
    }

    public static StringBuilder joinObjects(CharSequence separator, final List<Object> parts){
        return join(separator, new AbstractList<CharSequence>() {
            @Override
            public CharSequence get(int location) {
                return parts.get(location).toString();
            }

            @Override
            public int size() {
                return parts.size();
            }
        });
    }

    public static StringBuilder joinObjects(CharSequence separator, final Object... parts){
        return joinObjects(separator, Arrays.asList(parts));
    }

    public static <T extends CharSequence> StringBuilder join(CharSequence separator, T[] parts){
        return join(separator, Arrays.asList(parts));
    }

    public static String joinObjectFields(Object object, String separator){
        return joinObjects(separator, Reflection.objectToPropertiesArray(object)).toString();
    }

    public static List<String> getObjectFieldValuesAsStringList(Object object){
        List<Field> fields = Reflection.getAllFields(object);
        List<String> result = new ArrayList<String>(fields.size());
        for(Field field : fields){
            Object value = Reflection.getValueOfField(object, field);
            result.add(value.toString());
        }

        return result;
    }

    public static String copyCharSequence(CharSequence charSequence) {
        StringBuilder stringBuilder = new StringBuilder(charSequence);
        return stringBuilder.toString();
    }

    public static CharSequence capitalize(CharSequence charSequence){
        return new TransformingString(charSequence, new Capitalizer());
    }

    public static String capitalizeAndCopy(CharSequence charSequence) {
        return capitalize(charSequence).toString();
    }

    public static CharSequence makeFirstLetterLowerCase(CharSequence charSequence){
        return new TransformingString(charSequence, new FirstLetterLowerCaseMaker());
    }

    public static String replaceAllIfNotSuccessNull(String replacement, String from, String replaceTo,
                                                    boolean ignoreSpaces){
        if(from.contains(replacement)){
            return from.replace(replacement, replaceTo);
        } else if(ignoreSpaces) {
            replacement = replacement.replaceAll(" ", "");
            if(from.contains(replacement)){
                return from.replace(replacement, replaceTo);
            }
        }

        return null;
    }

    public static final String setCharAt(String string, int index, char ch){
        char[] array = string.toCharArray();
        array[index] = ch;
        return new String(array);
    }

    public static long getLongFromString(String string) {
        return Long.parseLong(string.replaceAll("[\\D]", ""));
    }

    public static String toString(Object o) {
        return o == null ? "" : o.toString();
    }

    public static StringBuilder copySubSequence(CharSequence charSequence, int start, int end) {
        if(end - start < 0){
            throw new IllegalArgumentException("end - start < 0");
        }

        if(end >= charSequence.length()) {
            throw new IllegalArgumentException("end >= charSequence.length()");
        }

        return new StringBuilder(new SubSequence(charSequence, start, end));
    }

    public static boolean charSequenceEquals(CharSequence a, CharSequence b) {
        if(a == null){
            return b == null;
        }

        if(b == null){
            return false;
        }

        int aLength = a.length();

        if(aLength != b.length()){
            return false;
        }

        for (int i = 0; i < aLength; i++) {
            if(a.charAt(i) != b.charAt(i)){
                return false;
            }
        }

        return true;
    }

    public static String quote(String string, String quote) {
        return quote + string + quote;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String repeat(char ch, int count) {
        if(count < 0){
            throw new IllegalArgumentException();
        }

        if(count == 0){
            return "";
        }

        char[] array = new char[count];
        for (int i = 0; i < count; i++) {
            array[i] = ch;
        }

        return new String(array);
    }

    public static List<String> splitInStringsWithLength(String string, int length) {
        List<String> result = new ArrayList<String>();
        int stringLength = string.length();
        for(int i = 0; i < stringLength; i += length){
            int end = i + length;
            if(stringLength - i < length){
                end = stringLength;
            }

            result.add(string.substring(i, end));
        }

        return result;
    }
}
