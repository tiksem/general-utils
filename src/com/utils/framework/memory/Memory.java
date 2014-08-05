package com.utils.framework.memory;

import com.utils.framework.Reflection;
import com.utils.framework.collections.ObjectAddressSet;
import com.utils.framework.collections.cache.Cache;
import com.utils.framework.collections.cache.LruCache;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 13.03.13
 * Time: 22:36
 * To change this template use File | Settings | File Templates.
 */
public class Memory {
    public static final int INT_SIZE = 4;
    public static final int LONG_SIZE = 8;
    public static final int BYTE_SIZE = 1;
    public static final int BOOLEAN_SIZE = 1;
    public static final int CHAR_SIZE = 2;
    public static final int FLOAT_SIZE = 4;
    public static final int DOUBLE_SIZE = 8;
    public static final int POINTER_SIZE = 8;
    public static final int MINIMUM_OBJECT_SIZE = 8;

    private static final int FIELDS_CACHE_SIZE = 20;

    private static final int[] powerOf2 = new int[]{
            2,
            2*2,
            2*2*2,
            2*2*2*2,
            2*2*2*2*2,
            2*2*2*2*2*2,
            2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2,2,
            2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2,
            2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2*2
    };

    private Cache<Class,List<Field>> fieldsCache = new LruCache<Class, List<Field>>(FIELDS_CACHE_SIZE);

    private static Memory instance;

    public static Memory getInstance(){
        if(instance == null){
            instance = new Memory();
        }

        return instance;
    }

    public int getObjectAlignSize(int size){
        if(size <= MINIMUM_OBJECT_SIZE){
            return MINIMUM_OBJECT_SIZE;
        }

        int index = Arrays.binarySearch(powerOf2, size);
        if(index < 0){
            index = -index - 1;
        }

        return powerOf2[index];
    }

    public int sizeOfPrimitive(Class type){
        if(type == int.class){
            return INT_SIZE;
        } else if(type == long.class){
            return LONG_SIZE;
        } else if(type == byte.class) {
            return BYTE_SIZE;
        } else if(type == boolean.class) {
            return BOOLEAN_SIZE;
        } else if(type == char.class) {
            return CHAR_SIZE;
        } else if(type == float.class) {
            return FLOAT_SIZE;
        } else if(type == double.class) {
            return DOUBLE_SIZE;
        } else {
            throw new IllegalArgumentException(type.getCanonicalName() +
                    " is not a primitive");
        }
    }

    public int sizeOfPrimitiveArray(Object array){
        Class type = array.getClass();

        if(type == int[].class){
            return INT_SIZE * ((int[])array).length;
        } else if(type == long[].class){
            return LONG_SIZE * ((long[])array).length;
        } else if(type == byte[].class) {
            return BYTE_SIZE * ((byte[])array).length;
        } else if(type == boolean[].class) {
            return BOOLEAN_SIZE * ((boolean[])array).length;
        } else if(type == char[].class) {
            return CHAR_SIZE * ((char[])array).length;
        } else if(type == float[].class) {
            return FLOAT_SIZE * ((float[])array).length;
        } else if(type == double[].class) {
            return DOUBLE_SIZE * ((double[])array).length;
        } else {
            return -1;
        }
    }

    private int sizeOfString(String string){
        return Math.round((string.length() * CHAR_SIZE))
                + POINTER_SIZE + INT_SIZE * 3;
    }

    private int sizeOfArray(Object array, ObjectAddressSet excludedInstances){
        int size = sizeOfPrimitiveArray(array);
        if(size == -1){
            size = 0;
        } else {
            return size;
        }

        for(Object o : (Object[])array){
            size += sizeOf(o, excludedInstances);
        }

        return size;
    }

    private int sizeOf(Object object, ObjectAddressSet excludedInstances){
        int size = 0;
        if(object == null || excludedInstances.contains(object)){
            return POINTER_SIZE;
        }

        Class classOfObject = object.getClass();

        if(classOfObject == String.class){
            size += sizeOfString((String)object);
        } else if(classOfObject.isArray()) {
            size += sizeOfArray(object, excludedInstances);
        } else {
            size += sizeOfObject(object, excludedInstances);
        }

        excludedInstances.add(object);
        return size;
    }

    public int sizeOf(Object object){
        return sizeOf(object, new ObjectAddressSet());
    }

    private List<Field> getFields(Object object){
        List<Field> fields = fieldsCache.get(object.getClass());
        if(fields == null){
            fields = Reflection.getAllFields(object);
            fieldsCache.put(object.getClass(), fields);
        }

        return fields;
    }

    private int sizeOfObject(Object object, ObjectAddressSet excludedInstances){
        if(object == null){
            return POINTER_SIZE;
        }

        int size = 0;
        List<Field> fields = getFields(object);

        for(Field field : fields){
            if(Modifier.isStatic(field.getModifiers())){
                continue;
            }

            Class fieldType = field.getType();
            if(fieldType.isPrimitive()){
                size += sizeOfPrimitive(fieldType);
            } else {
                Object property = Reflection.getValueOfField(object, field);
                size += sizeOf(property, excludedInstances);
            }
        }

        return getObjectAlignSize(size) + POINTER_SIZE;
    }
}
