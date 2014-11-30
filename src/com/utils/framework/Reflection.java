package com.utils.framework;

import com.utils.framework.strings.Strings;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
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

    public static List<Method> getAllMethodsOfClass(Class aClass){
        ArrayList<Method> methods = new ArrayList<Method>();

        while(aClass != Object.class){
            List<Method> methodList = Arrays.asList(aClass.getDeclaredMethods());
            methods.addAll(methodList);
            aClass = aClass.getSuperclass();
        }

        return methods;
    }

    public static List<Method> getAllMethods(Object object){
        Class objectClass = object.getClass();
        return getAllMethodsOfClass(objectClass);
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

    public static boolean setValueOfFieldIfNull(Object object, Field field, Object value){
        if (getFieldValueUsingGetter(object, field) == null) {
            setFieldValueUsingSetter(object, field, value);
            return true;
        }

        return false;
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

    public static List<Field> getFieldsWithAnnotations(Class aClass,
                                                       final Iterable<? extends Class> annotationClasses) {
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

    public static List<Field> getFieldsWithAnnotations(Class aClass,
                                                       final Class... annotationClasses) {
        return getFieldsWithAnnotations(aClass, Arrays.asList(annotationClasses));
    }

    public static List<Field> getFieldsWithAndWithoutAnnotations(Class aClass,
                                                                 List<? extends Class> with,
                                                                 List<? extends Class> without) {
        List<Field> withList = getFieldsWithAnnotations(aClass, with);
        final List<Field> withoutList = getFieldsWithAnnotations(aClass, without);
        withList.removeAll(withoutList);
        return withList;
    }

    public static Field getFieldWithAnnotation(Class aClass, final Class annotation) {
        return CollectionUtils.find(getAllFieldsOfClass(aClass), new Predicate<Field>() {
            @Override
            public boolean check(Field item) {
                return item.getAnnotation(annotation) != null;
            }
        });
    }

    private final static char DOT = '.';
    private final static char SLASH = '/';
    private final static String CLASS_SUFFIX = ".class";
    private final static String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the given '%s' package exists?";

    public static String[] classesCanonicalAsNamesStringArray(Collection<Class<?>> classes) {
        String[] result = new String[classes.size()];
        int index = 0;

        for(Class aClass : classes){
            result[index++] = aClass.getCanonicalName();
        }

        return result;
    }

    public static List<Class<?>> findClassesInPackage(final String scannedPackage) {
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final String scannedPath = scannedPackage.replace(DOT, SLASH);
        final Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(scannedPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage), e);
        }

        final List<Class<?>> classes = new ArrayList<Class<?>>();
        while (resources.hasMoreElements()) {
            final File file = new File(resources.nextElement().getFile());
            classes.addAll(findClassesInPackage(file, scannedPackage));
        }

        return classes;
    }

    private static List<Class<?>> findClassesInPackage(final File file, final String scannedPackage) {
        final List<Class<?>> classes = new LinkedList<Class<?>>();
        final String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory()) {
            for (File nestedFile : file.listFiles()) {
                classes.addAll(findClassesInPackage(nestedFile, scannedPackage));
            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            final int beginIndex = 0;
            final int endIndex = resource.length() - CLASS_SUFFIX.length();
            final String className = resource.substring(beginIndex, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

    public static <T> T cloneObject(T object) {
        try {
            T result = (T) object.getClass().newInstance();
            List<Field> fields = getAllFields(object);
            for(Field field : fields){
                if (!Modifier.isStatic(field.getModifiers())) {
                    Object value = getValueOfField(object, field);
                    setValueOfField(result, field, value);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> cloneObjects(Iterable<? extends T> objects) {
        List<T> result = new ArrayList<T>();
        for(T object : objects) {
            result.add(cloneObject(object));
        }

        return result;
    }

    public static boolean hasField(Class aClass, final String fieldName) {
        return CollectionUtils.find(getAllFieldsOfClass(aClass), new Predicate<Field>() {
            @Override
            public boolean check(Field item) {
                return item.getName().equals(fieldName);
            }
        }) != null;
    }

    public static Field getFieldByNameOrThrow(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValueUsingGetter(Object object, String fieldName) {
        Field field = getFieldByNameOrThrow(object, fieldName);
        return getFieldValueUsingGetter(object, field);
    }

    public static Object getFieldValueUsingGetter(Object object, Field field) {
        Class aClass = object.getClass();
        String getterName = "get" + Strings.capitalize(field.getName());
        try {
            Method getter = aClass.getDeclaredMethod(getterName);
            getter.setAccessible(true);
            return getter.invoke(object);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValueUsingSetter(Object object, Field field, Object value) {
        Class aClass = object.getClass();
        String setterName = "set" + Strings.capitalize(field.getName());
        try {
            Method setter = aClass.getDeclaredMethod(setterName, field.getType());
            setter.setAccessible(true);
            setter.invoke(object, value);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
