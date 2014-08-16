package org.kasource.cdi.extension.beanlisten.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a method with this annotation to get notified when a bean is created.
 * 
 * The method should be public and taking Object as its only parameter.
 * 
 * Note that this annotation will not have any affect if the class is not annotated with @BeanListener.
 * 
 * @author rikardwi
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnBeanPostConstruct {

}
