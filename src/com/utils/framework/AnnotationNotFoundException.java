package com.utils.framework;

/**
 * Created by CM on 12/28/2014.
 */
public class AnnotationNotFoundException extends RuntimeException {
    public AnnotationNotFoundException(Class aClass, String fieldName) {
        super("Annotation " + aClass.getCanonicalName() + " was not found in field" + fieldName);
    }
}
