package org.kasource.cdi.util;

import java.lang.annotation.Annotation;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Annotation utilities.
 * 
 * 
 * @author rikardwi
 **/
public final class AnnotationUtil {
    
    private AnnotationUtil(){};
   
    /**
     * Creates an annotation from an annotation class.
     * 
     * To create an annotation with attributes use org.kasource.commons.reflection.annotation.AnnotationBuilder from
     * the ka commons reflection artifact.
     * 
     * @param <T> Annotation Type
     * @param annoClass Annotation to create instance of
     * 
     * @return An AnnotationLiteral implementation.
     **/
    @SuppressWarnings({ "unchecked", "serial" })
    public static <T extends Annotation> T getAnnotation(final Class<T> annoClass) {
        return (T) new AnnotationLiteral<T>() {
                
            @Override
            public Class<? extends Annotation> annotationType() {
                return annoClass;
            }
            
        };
    }
}

