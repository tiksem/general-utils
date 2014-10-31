package com.utils.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * Date: 13.03.13
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public final class Reflection {
    public static List<Field> getAllFieldsOfClass(Class aClass){
        ArrayList<Field> fields = new ArrayList<Field>();

        while(aClass != Object.class){
            List<Field> fieldList = Arrays.asList(aClass.getDeclaredFields());
            fields.addAll(fieldList);
            aClass = aClass.getSuperclass();
        }

        return fields;
    }

    public static List<Field> getAllFields(Object object){
        Class objectClass = object.getClass();
        return getAllFieldsOfClass(objectClass);
    }

    public static Object getValueOfField(Object object, Field field){
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static void setValueOfField(Object object, Field field, Object value){
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Iterable<Object> getObjectProperties(final Object object){
        final List<Field> fields = getAllFields(object);
        return new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {

                return new Iterator<Object>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < fields.size();
                    }

                    @Override
                    public Object next() {
                        Field field = fields.get(index++);
                        field.setAccessible(true);
                        return getValueOfField(object, field);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };

            }
        };
    }

    public static interface ParamTransformer{
        Object transform(String paramName, Object value);
    }

    public static Map<String, Object> objectToPropertyMap(Object object, ParamTransformer paramTransformer) {
        Map<String, Object> result = new HashMap<String, Object>();
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getFields();
        try {
            for (Field field : fields) {
                Object value = field.get(object);
                String key = field.getName();

                if(paramTransformer != null){
                    value = paramTransformer.transform(key, value);
                }

                if (value == null) {
                    continue;
                }

                result.put(key, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static Map<String, Object> objectToPropertyMap(Object object) {
        return objectToPropertyMap(object,null);
    }

    public static Object[] objectToPropertiesArray(Object object){
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getFields();
        Object[] result = new Object[fields.length];

        for(int i = 0; i < fields.length;){
            Field field = fields[i];
            if(Modifier.isTransient(field.getModifiers())){
                continue;
            }

            field.setAccessible(true);
            try {
                result[i] = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            i++;
        }

        return result;
    }

    public static <T> T createObjectOfClass(Class<T> type, Object... params) throws Throwable {
        Constructor[] constructors = type.getConstructors();
        for (Constructor constructor : constructors) {
            try {
                T object = (T) constructor.newInstance(params);
                return object;
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
        throw new RuntimeException("no appropriate constructor available");
    }

    public static List<Field> getFieldsWithAnnotations(Class aClass, final Class... annotationClasses) {
        List<Field> allFields = getAllFieldsOfClass(aClass);
        return CollectionUtils.findAll(allFields, new Predicate<Field>() {
            @Override
            public boolean check(Field item) {
                for (Class annotationClass : annotationClasses) {
                    if(item.getAnnotation(annotationClass) != null){
                        return true;
                    }
                }

                return false;
            }
        });
    }
}
