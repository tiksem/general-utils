package com.utils.framework;

import com.utils.framework.collections.SetWithPredicates;
import com.utils.framework.collections.map.ListValuesMultiMap;
import com.utils.framework.collections.map.MultiMap;
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

    public static List<Field> getAllFieldsExcluding(Object object, final Collection<String> names){
        List<Field> fields = getAllFields(object);
        CollectionUtils.removeAll(fields, new Predicate<Field>() {
            @Override
            public boolean check(Field item) {
                return names.contains(item.getName());
            }
        });
        return fields;
    }

    public static List<Field> getAllFieldsExcluding(Object object, final String... names){
        return getAllFieldsExcluding(object, Arrays.asList(names));
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
            throw new RuntimeException(e);
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
        Object transform(Field field, Object value);
    }

    public static Map<String, Object> fieldsToPropertyMap(Object object, List<Field> fields) {
        return fieldsToPropertyMap(object, fields, null);
    }

    public static void fieldsToPropertyMap(Map<String, Object> map, Object object, List<Field> fields,
                                                          ParamTransformer paramTransformer) {
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                String key = field.getName();

                if(paramTransformer != null){
                    value = paramTransformer.transform(field, value);
                }

                if (value == null) {
                    continue;
                }

                map.put(key, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> fieldsToPropertyMap(Object object, List<Field> fields,
                                                          ParamTransformer paramTransformer) {
        Map<String, Object> result = new HashMap<String, Object>();
        fieldsToPropertyMap(result, object, fields, paramTransformer);
        return result;
    }

    public static void objectToPropertyMap(Map<String, Object> map, Object object) {
        objectToPropertyMap(map, object, null);
    }

    public static void objectToPropertyMap(Map<String, Object> map, Object object, ParamTransformer paramTransformer) {
        List<Field> fields = getAllFields(object);
        fieldsToPropertyMap(map, object, fields, paramTransformer);
    }

    public static Map<String, Object> objectToPropertyMap(Object object, ParamTransformer paramTransformer) {
        List<Field> fields = getAllFields(object);
        return fieldsToPropertyMap(object, fields, paramTransformer);
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

    public static <T> List<Class<T>> getClasses(List<T> objects) {
        return CollectionUtils.transform(objects, new CollectionUtils.Transformer<T, Class<T>>() {
            @Override
            public Class<T> get(T t) {
                return (Class<T>) t.getClass();
            }
        });
    }

    public static <T> List<Class<T>> getClasses(T... objects) {
        return getClasses(Arrays.asList(objects));
    }

    public static <T> T createObjectOfClass(Class<T> type, Object... params) {
        if(params.length == 0){
            try {
                return type.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        Constructor[] constructors = type.getConstructors();
        for (Constructor constructor : constructors) {
            try {
                return (T) constructor.newInstance(params);
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
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

    public static MultiMap<Class, Field> getFieldsWithAnnotationsMap(Class aClass,
                                                                     final Class... annotationClasses) {
        MultiMap<Class, Field> result = new ListValuesMultiMap<Class, Field>();
        for(Class annotationClass : annotationClasses){
            List<Field> fields = getFieldsWithAnnotations(aClass, annotationClasses);
            result.putAll(annotationClass, fields);
        }

        return result;
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

    public static <T extends Annotation> T getAnnotationOrThrow(Field field, Class<T> aClass) {
        T annotation = field.getAnnotation(aClass);
        if(annotation == null){
            throw new AnnotationNotFoundException(aClass, field.getName());
        }

        return annotation;
    }

    public static Field getFieldWithAnnotation(Class aClass, final Class annotation) {
        return getFieldWithAnnotation(getAllFieldsOfClass(aClass), annotation);
    }

    public static Field getFieldWithAnnotation(Iterable<Field> fields, final Class annotation) {
        return CollectionUtils.find(fields, new Predicate<Field>() {
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

    public static void setFieldsFromMap(Object object, Map<String, Object> map) {
        setFieldsFromMap(object, getAllFields(object), map);
    }

    public static void setFieldsFromMap(Object object, List<Field> fields, Map<String, Object> map) {
        for(Field field : fields){
            Object value = map.get(field.getName());
            if(value != null){
                setFieldValueUsingSetter(object, field, value);
            }
        }
    }

    public static boolean hasField(Class aClass, final String fieldName) {
        return CollectionUtils.find(getAllFieldsOfClass(aClass), new Predicate<Field>() {
            @Override
            public boolean check(Field item) {
                return item.getName().equals(fieldName);
            }
        }) != null;
    }

    public static boolean hasOneOrMoreAnnotations(Field field, Class... annotations) {
        for (Class annotation : annotations) {
            if(field.getAnnotation(annotation) != null){
                return true;
            }
        }

        return false;
    }

    public static Field getFieldByNameOrThrow(Object object, String fieldName) {
        return getFieldByNameOrThrow(object.getClass(), fieldName);
    }

    public static Field getFieldByNameOrThrow(Class aClass, String fieldName) {
        try {
            return aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValueUsingGetter(Object object, String fieldName) {
        Field field = getFieldByNameOrThrow(object, fieldName);
        return getFieldValueUsingGetter(object, field);
    }

    public static boolean isNull(Object object, Field field) {
        return getValueOfField(object, field) == null;
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

    public static Object getOrCreateFieldValue(Object object, Field field, Object... constructorParams) {
        Object value = getFieldValueUsingGetter(object, field);
        if(value == null){
            value = createObjectOfClass(field.getType(), constructorParams);
            setFieldValueUsingSetter(object, field, value);
        }

        return value;
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

    public static <T> Set<Class<T>> classNameSetIgnoreCase() {
        Equals<Class<T>> equals = new Equals<Class<T>>() {
            @Override
            public boolean equals(Class<T> a, Class<T> b) {
                return a.getName().equalsIgnoreCase(b.getName());
            }
        };
        HashCodeProvider<Class<T>> hashCodeProvider = new HashCodeProvider<Class<T>>() {
            @Override
            public int getHashCodeOf(Class<T> object) {
                return object.getName().toLowerCase().hashCode();
            }
        };
        return new SetWithPredicates<Class<T>>(equals, hashCodeProvider);
    }

    public static Set<Field> fieldNameSetIgnoreCase() {
        Equals<Field> equals = new Equals<Field>() {
            @Override
            public boolean equals(Field a, Field b) {
                return a.getName().equalsIgnoreCase(b.getName());
            }
        };
        HashCodeProvider<Field> hashCodeProvider = new HashCodeProvider<Field>() {
            @Override
            public int getHashCodeOf(Field object) {
                return object.getName().toLowerCase().hashCode();
            }
        };
        return new SetWithPredicates<Field>(equals, hashCodeProvider);
    }

    public static List<Method> getMethodsWithAnnotation(Class aClass, final Class annotationClass) {
        return CollectionUtils.findAll(
                getAllMethodsOfClass(aClass), new Predicate<Method>() {
                    @Override
                    public boolean check(Method item) {
                        return item.getAnnotation(annotationClass) != null;
                    }
                });
    }

    public static Object executeMethod(Object thisObject, Method method, Object... params) {
        try {
            method.setAccessible(true);
            return method.invoke(thisObject, params);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeMethodsWithAnnotation(Object thisObject, Class annotationClass,
                                                      Object... params) {
        List<Method> methods = getMethodsWithAnnotation(thisObject.getClass(), annotationClass);
        for(Method method : methods){
            executeMethod(thisObject, method, params);
        }
    }

    public static void setValuesOfFieldsWithAnnotation(Object object, Object value,
                                                       Class annotationClass,
                                                       Predicate<Object> condition) {
        List<Field> fields = getFieldsWithAnnotations(object.getClass(), annotationClass);
        for(Field field : fields){
            Object fieldCurrentValue = getFieldValueUsingGetter(object, field);
            if(condition == null || condition.check(fieldCurrentValue)) {
                setFieldValueUsingSetter(object, field, value);
            }
        }
    }

    public static void setValuesOfFieldsWithAnnotation(Object object, Object value,
                                                       Class annotationClass) {
        setValuesOfFieldsWithAnnotation(object, value, annotationClass, null);
    }

    public static void setValuesOfFieldsWithAnnotationIfNull(Object object, Object value,
                                                       Class annotationClass) {
        setValuesOfFieldsWithAnnotation(object, value, annotationClass, new Predicate<Object>() {
            @Override
            public boolean check(Object item) {
                return item == null;
            }
        });
    }
}
