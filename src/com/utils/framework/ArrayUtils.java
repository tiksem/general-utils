package com.utils.framework;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by CM on 2/13/2015.
 */
public class ArrayUtils {
    public interface ObjectToByte<From> {
        byte transform(From from);
    }

    public static <From> byte[] transformToBytes(From[] from, ObjectToByte<From> transformer) {
        int length = from.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = transformer.transform(from[i]);
        }

        return result;
    }

    public interface ByteToObject<To> {
        To transform(byte from);
    }

    public static <To> To[] transformBytesToObjects(byte[] from, ByteToObject<To> transformer) {
        int length = from.length;
        To[] result = (To[]) new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = transformer.transform(from[i]);
        }

        return result;
    }

    public static byte[] stringsToBytes(String[] strings) {
        return transformToBytes(strings, new ObjectToByte<String>() {
            @Override
            public byte transform(String s) {
                return Byte.parseByte(s);
            }
        });
    }

    public static String[] bytesToStrings(byte[] bytes) {
        return transformBytesToObjects(bytes, new ByteToObject<String>() {
            @Override
            public String transform(byte from) {
                return String.valueOf((int)from);
            }
        });
    }

    public static int byteArrayToInt(byte[] array, ByteOrder byteOrder) {
        return ByteBuffer.wrap(array).order(byteOrder).getInt();
    }

    public static int byteArrayToInt(byte[] array) {
        return byteArrayToInt(array, ByteOrder.BIG_ENDIAN);
    }

    public static byte[] intToByteArray(int integer, ByteOrder byteOrder) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(byteOrder);
        byteBuffer.putInt(integer);
        return byteBuffer.array();
    }

    public static byte[] intToByteArray(int integer) {
        return intToByteArray(integer, ByteOrder.BIG_ENDIAN);
    }

    public static int count(char[] array, char ch) {
        int count = 0;
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if(array[i] == ch){
                count++;
            }
        }

        return count;
    }

    public static int indexOf(char[] array, char ch, int from) {
        int length = array.length;
        for (int i = from; i < length; i++) {
            if(array[i] == ch){
                return i;
            }
        }

        return -1;
    }
}
