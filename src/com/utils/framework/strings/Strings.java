package com.utils.framework.strings;

import com.utils.framework.ArrayUtils;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Reflection;
import com.utils.framework.collections.iterator.AbstractIterator;
import com.utils.framework.strings.Capitalizer;
import com.utils.framework.strings.FirstLetterLowerCaseMaker;
import com.utils.framework.strings.TransformingString;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 12:48
 * To change this template use File | Settings | File Templates.
 */
public class Strings {
    private static final Pattern ONLY_SPACES = Pattern.compile("\\s*");

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

        for (int i = partsSize - 1; i >= 0; i--, partsSize--) {
            if(parts.get(i) != null){
                break;
            }
        }

        if(partsSize == 0){
            return;
        }

        for (int i = 0; i < partsSize; i++) {
            T part = parts.get(i);

            if (part != null) {
                out.append(part);
                if(i < partsSize - 1){
                    out.append(separator);
                }
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
        int length = charSequence.length();
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            array[i] = charSequence.charAt(i);
        }

        return new String(array);
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

    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() <= 0;
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

    public static int findUnsignedIntegerInString(String string) {
        try {
            return Integer.valueOf(string.replaceAll("[^\\d]+", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static char[] replaceCharArray(char[] string, int begin, int end, char[] replacement) {
        if(begin < 0 || end > string.length){
            throw new IllegalArgumentException("begin < 0 || end > string.length()");
        }

        int resultLength = string.length - end + begin + replacement.length;
        char[] result = new char[resultLength];

        System.arraycopy(string, 0, result, 0, begin);
        System.arraycopy(replacement, 0, result, begin, replacement.length);
        System.arraycopy(string, end, result, begin + replacement.length, string.length - end);

        return result;
    }

    public static String replace(String string, int begin, int end, String replacement) {
        return String.valueOf(replaceCharArray(string.toCharArray(), begin, end, replacement.toCharArray()));
    }

    public static String replace(String string, Matcher matcher, String replacement) {
        return replace(string, matcher.start(), matcher.end(), replacement);
    }

    public static List<String> findAll(CharSequence string, Pattern pattern) {
        final Matcher matcher = pattern.matcher(string);
        List<String> result = new ArrayList<String>();
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if(a != null){
            return a.equalsIgnoreCase(b);
        } else {
            return b == null;
        }
    }

    public static boolean hasOnlySpaceCharacters(String string) {
        return ONLY_SPACES.matcher(string).matches();
    }

    public static char[][] split(char[] string, char delimiter) {
        int size = ArrayUtils.count(string, delimiter) + 1;
        char[][] result = new char[size][];

        int begin = 0;
        int end;
        int index = 0;
        boolean exitLoop = false;

        while (true) {
            end = ArrayUtils.indexOf(string, delimiter, begin);
            if(end < 0){
                exitLoop = true;

            }

            if(exitLoop){
                break;
            }
        }

        throw new UnsupportedOperationException("Sorry, not implemented yet");
    }

    public static String getFirstStringBetweenQuotes(String string, String quote1, String quote2) {
        int a = string.indexOf(quote1);
        if(a < 0){
            return null;
        }
        a += quote1.length();

        int b = string.indexOf(quote2, a);
        if(b < 0){
            return null;
        }

        return string.substring(a, b);
    }

    public static String getFirstStringBetweenQuotes(String string, String quote) {
        return getFirstStringBetweenQuotes(string, quote, quote);
    }

    public interface Replacer {
        String getReplacement(String source);
    }

    public static void regexpReplace(StringBuilder result, String string, Pattern pattern, Replacer replacer) {
        Matcher matcher = pattern.matcher(string);
        int startIndex = 0;
        while (matcher.find(startIndex)) {
            int start = matcher.start();
            int end = matcher.end();

            String value = string.substring(start, end);
            String replacement = replacer.getReplacement(value);
            result.append(string.substring(startIndex, start));
            result.append(replacement);

            startIndex = end;
        }

        result.append(string.substring(startIndex));
    }

    public static String regexpReplace(String string, Pattern pattern, Replacer replacer) {
        StringBuilder stringBuilder = new StringBuilder();
        regexpReplace(stringBuilder, string, pattern, replacer);
        return stringBuilder.toString();
    }

    public static String splitReplaceAndJoin(String delimiter, String joinDelimiter,
                                             String string, final Replacer replacer) {
        String[] split = string.split(delimiter);
        List<String> transform = CollectionUtils.transform(Arrays.asList(split),
                new CollectionUtils.Transformer<String, String>() {
            @Override
            public String get(String s) {
                return replacer.getReplacement(s);
            }
        });
        return join(joinDelimiter, transform).toString();
    }
}
