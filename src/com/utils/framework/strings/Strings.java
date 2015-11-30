package com.utils.framework.strings;

import com.utils.framework.*;

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
    
    public static String joinObjects(List<? extends Object> objects, char separator) {
        return join(objectsToStringsView(objects), separator);
    }

    public static List<String> objectsToStringsView(final List<? extends Object> objects) {
        return new AbstractList<String>() {
            @Override
            public String get(int location) {
                return objects.get(location).toString();
            }

            @Override
            public int size() {
                return objects.size();
            }
        };
    }

    public static String join(Collection<String> parts, char separator) {
        if (parts.isEmpty()) {
            return "";
        }

        int length = length(parts);
        length += parts.size() - 1;
        char[] result = new char[length];
        int resultIndex = 0;

        for (String part : parts) {
            int partLength = part.length();
            part.getChars(0, partLength, result, resultIndex);
            resultIndex += partLength;
            if (resultIndex != length) {
                result[resultIndex++] = separator;
            }
        }

        return new String(result);
    }

    public static String join(String[] parts, char separator) {
        if (parts.length == 0) {
            return "";
        }

        int length = length(parts);
        length += parts.length - 1;
        char[] result = new char[length];
        int resultIndex = 0;

        for (String part : parts) {
            int partLength = part.length();
            part.getChars(0, partLength, result, resultIndex);
            resultIndex += partLength;
            if (resultIndex != length) {
                result[resultIndex++] = separator;
            }
        }

        return new String(result);
    }

    public static <T extends CharSequence> int length(Collection<T> sequences) {
        int size = 0;
        for (CharSequence charSequence : sequences) {
            if (charSequence != null) {
                size += charSequence.length();
            }
        }
        return size;
    }

    public static int length(CharSequence[] sequences) {
        return length(Arrays.asList(sequences));
    }

    public static int length(String[] sequences) {
        return length(Arrays.asList(sequences));
    }

    public static <T extends CharSequence> void join(CharSequence separator, List<T> parts,
                                                     StringBuilder out) {
        int partsSize = parts.size();

        for (int i = partsSize - 1; i >= 0; i--, partsSize--) {
            if (parts.get(i) != null) {
                break;
            }
        }

        if (partsSize == 0) {
            return;
        }

        for (int i = 0; i < partsSize; i++) {
            T part = parts.get(i);

            if (part != null) {
                out.append(part);
                if (i < partsSize - 1) {
                    out.append(separator);
                }
            }
        }
    }

    public static <T extends CharSequence> void join(CharSequence separator, Iterator<T> parts,
                                                     StringBuilder out) {
        while (parts.hasNext()) {
            T next = parts.next();
            if (next != null) {
                out.append(next);
                 if (parts.hasNext()) {
                     out.append(separator);
                 }
            }
        }
    }

    public static <T extends CharSequence> StringBuilder join(CharSequence separator, List<T> parts) {
        StringBuilder result = new StringBuilder();
        join(separator, parts, result);
        return result;
    }

    public static <T extends CharSequence> StringBuilder join(CharSequence separator, Iterator<T> iterator) {
        StringBuilder result = new StringBuilder();
        join(separator, iterator, result);
        return result;
    }

    public static StringBuilder joinObjects(CharSequence separator, final List<Object> parts) {
        return join(separator, objectsToStringsView(parts));
    }

    public static StringBuilder joinObjects(CharSequence separator, final Object... parts) {
        return joinObjects(separator, Arrays.asList(parts));
    }

    public static <T extends CharSequence> StringBuilder join(CharSequence separator, T[] parts) {
        return join(separator, Arrays.asList(parts));
    }

    public static String copyCharSequence(CharSequence charSequence) {
        int length = charSequence.length();
        char[] array = new char[length];
        for (int i = 0; i < length; i++) {
            array[i] = charSequence.charAt(i);
        }

        return new String(array);
    }

    public static CharSequence capitalizeCharSequence(CharSequence charSequence) {
        return capitalize(charSequence);
    }

    public static CharSequence capitalize(CharSequence charSequence) {
        return new TransformingString(charSequence, new Capitalizer());
    }

    public static String capitalize(String string) {
        return setCharAt(string, 0, Character.toUpperCase(string.charAt(0)));
    }

    public static String capitalizeAndCopy(CharSequence charSequence) {
        return capitalize(charSequence).toString();
    }

    public static CharSequence makeFirstLetterLowerCase(CharSequence charSequence) {
        return new TransformingString(charSequence, new FirstLetterLowerCaseMaker());
    }

    public static String replaceAllIfNotSuccessNull(String replacement, String from, String replaceTo,
                                                    boolean ignoreSpaces) {
        if (from.contains(replacement)) {
            return from.replace(replacement, replaceTo);
        } else if (ignoreSpaces) {
            replacement = replacement.replaceAll(" ", "");
            if (from.contains(replacement)) {
                return from.replace(replacement, replaceTo);
            }
        }

        return null;
    }

    public static String setCharAt(String string, int index, char ch) {
        char[] array = string.toCharArray();
        array[index] = ch;
        return new String(array);
    }

    public static String replaceFirstChar(String string, char ch, char replacement) {
        int index = string.indexOf(ch);
        if (index >= 0) {
            return setCharAt(string, index, replacement);
        }

        return string;
    }

    public static long getLongFromString(String string) {
        return Long.parseLong(string.replaceAll("[\\D]", ""));
    }

    public static int getFirstUnsignedInteger(CharSequence string) {
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch >= '1' && ch <= '9') {
                return parseUnsignedIntToken(string, i);
            }
        }

        return -1;
    }

    public static String toString(Object o) {
        return o == null ? "" : o.toString();
    }

    public static StringBuilder copySubSequence(CharSequence charSequence, int start, int end) {
        if (end - start < 0) {
            throw new IllegalArgumentException("end - start < 0");
        }

        if (end >= charSequence.length()) {
            throw new IllegalArgumentException("end >= charSequence.length()");
        }

        return new StringBuilder(new SubSequence(charSequence, start, end));
    }

    public static boolean charSequenceEquals(CharSequence a, CharSequence b) {
        if (a == null) {
            return b == null;
        }

        if (b == null) {
            return false;
        }

        int aLength = a.length();

        if (aLength != b.length()) {
            return false;
        }

        for (int i = 0; i < aLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
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

    public static boolean containsAnyNotEmpty(CharSequence... charSequences) {
        for (CharSequence charSequence : charSequences) {
            if (charSequence.length() > 0) {
                return true;
            }
        }

        return false;
    }

    public static String repeat(char ch, int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }

        if (count == 0) {
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
        for (int i = 0; i < stringLength; i += length) {
            int end = i + length;
            if (stringLength - i < length) {
                end = stringLength;
            }

            result.add(string.substring(i, end));
        }

        return result;
    }

    public static int indexOfFirstDigit(CharSequence string) {
        int length = string.length();
        for (int i = 0; i < length; i++) {
            char charAt = string.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                return i;
            }
        }

        return -1;
    }

    public static int findUnsignedIntegerInString(CharSequence string) {
        int digitIndex = indexOfFirstDigit(string);
        if (digitIndex >= 0) {
            return parseUnsignedIntToken(string, digitIndex);
        } else {
            return -1;
        }
    }

    public static char[] replaceCharArray(char[] string, int begin, int end, char[] replacement) {
        if (begin < 0 || end > string.length) {
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
        if (a != null) {
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
            if (end < 0) {
                exitLoop = true;

            }

            if (exitLoop) {
                break;
            }
        }

        throw new UnsupportedOperationException("Sorry, not implemented yet");
    }

    public static String getFirstStringBetweenQuotes(String string, String quote1, String quote2) {
        int a = string.indexOf(quote1);
        if (a < 0) {
            return null;
        }
        a += quote1.length();

        int b = string.indexOf(quote2, a);
        if (b < 0) {
            return null;
        }

        return string.substring(a, b);
    }

    public static String getFirstStringBetweenQuotes(String string, String quote) {
        return getFirstStringBetweenQuotes(string, quote, quote);
    }

    public static String[] toLowerCase(String[] source) {
        int length = source.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = source[i];
        }

        return result;
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
                new Transformer<String, String>() {
                    @Override
                    public String get(String s) {
                        return replacer.getReplacement(s);
                    }
                });
        return join(joinDelimiter, transform).toString();
    }

    public static int countOccurrences(CharSequence string, char ch) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    public static CharSequence limitCharOccurrences(CharSequence string, char ch, int limit) {
        int count = 0;
        int i = 0;

        for (; i < string.length(); i++) {
            if (string.charAt(i) == ch) {
                count++;
            }

            if (count > limit) {
                break;
            }
        }

        if (i != string.length()) {
            StringBuilder result = new StringBuilder();
            result.append(string, 0, i);
            return result;
        } else {
            return string;
        }
    }

    public static <A, B extends CharSequence> boolean stringListsEquals(Collection<A> first,
                                            Collection<B> second) {
        return CollectionUtils.contentEquals((Collection<CharSequence>)first, (Collection<CharSequence>)second,
                new Equals<CharSequence>() {
            @Override
            public boolean equals(CharSequence a, CharSequence b) {
                return charSequenceEquals(a, b);
            }
        });
    }

    public static int parseUnsignedIntToken(CharSequence value, int index) {
        int result = 0;

        for (; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (Character.isDigit(ch)) {
                result *= 10;
                result += Character.digit(ch, 10);
            } else {
                break;
            }
        }

        return result;
    }

    public static int indexOfIgnoreCase(List<String> strings, String occurrence) {
        int size = strings.size();
        for (int i = 0; i < size; i++) {
            if (strings.get(i).equalsIgnoreCase(occurrence)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean containsIgnoreCase(List<String> strings, String occurrence) {
        return indexOfIgnoreCase(strings, occurrence) >= 0;
    }

    public static boolean containsIgnoreCase(String string, String occurrence) {
        return string.toLowerCase().contains(occurrence.toLowerCase());
    }

    public static boolean containsAnyIgnoreCase(String string, String... occurrences) {
        string = string.toLowerCase();

        for (String occurrence : occurrences) {
            if (string.contains(occurrence.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static boolean containsAnyIgnoreCase(List<String> strings, String... occurrences) {
        for (String occurrence : occurrences) {
            if (containsIgnoreCase(strings, occurrence)) {
                return true;
            }
        }

        return false;
    }

    public static <T extends Object> String joinKeyValueMap(Map<String, T> map, final String delimiterBetweenKeyAndValue,
                                         String delimiterBetweenPairs) {
        Iterator<String> iterator = CollectionUtils.transform(map.entrySet().iterator(),
                new Transformer<Map.Entry<String, T>, String>() {
            @Override
            public String get(Map.Entry<String, T> entry) {
                return entry.getKey() + delimiterBetweenKeyAndValue + entry.getValue();
            }
        });

        return join(delimiterBetweenPairs, iterator).toString();
    }

    public static char getLast(CharSequence charSequence) {
        return charSequence.charAt(charSequence.length() - 1);
    }
}
