package org.kasource.cdi.bean;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.kasource.cdi.util.AnnotationUtil;
import org.kasource.di.bean.BeanNotFoundException;
import org.kasource.di.bean.BeanResolver;



@ApplicationScoped
public class CdiBeanResolver implements BeanResolver {

	@Inject
	private BeanManager beanManager;

	/**
	 * Returns the bean with matching bean name of the
	 * supplied type.
	 * 
	 * TODO: Fix producer methods
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> ofType)
			throws BeanNotFoundException {
	    try {
    		Set<Bean<?>> beans = beanManager.getBeans(beanName);
    		for(Bean<?> bean : beans) {
    		    T object = (T) beanManager.getReference(bean, ofType, beanManager.createCreationalContext(bean));
    		    if (ofType.isAssignableFrom(object.getClass())) {
    		        return object;
    		    }
    		}
	    } catch (Exception e) {
	        throw new BeanNotFoundException("Could not find any bean named: " + beanName + " of class: " + ofType, e);
        }
		throw new BeanNotFoundException("Could not find any bean named: " + beanName + " of class: " + ofType);
	}

   
	/**
	 * Returns all beans found with of the class supplied. 
	 * 
	 * TODO: Fix producer methods
	 */
	@Override
	public <T> Set<T> getBeans(Class<T> ofType) {
	    Set<T> setOfBeans = new HashSet<T>();
	    try {
            Set<Bean<?>> beans = beanManager.getBeans(ofType, AnnotationUtil.getAnnotation(Any.class));
            for(Bean<?> bean : beans) {
                try {
                    @SuppressWarnings("unchecked")
                    T object = (T) beanManager.getReference(bean, ofType, beanManager.createCreationalContext(bean));
                    setOfBeans.add(object);
                } catch (RuntimeException e) {
                    continue;
                }
            }
            return setOfBeans;
        } catch (IllegalArgumentException e) {
            return setOfBeans;
        }
	 }

    @Override
    public <T> Set<T> getBeansByQualifier(Class<T> ofType, Class<? extends Annotation>... qualifiers) {
        Annotation[] annotations = new Annotation[qualifiers.length];
        for (int i = 0; i < qualifiers.length; i++) {
            annotations[i] = AnnotationUtil.getAnnotation(qualifiers[i]);
        }
        return getBeansByQualifier(ofType, annotations);
    }

    @Override
    public <T> Set<T> getBeansByQualifier(Class<T> ofType, Annotation... qualifiers) {
        Set<T> setOfBeans = new HashSet<T>();
        try {
            Set<Bean<?>> beans = beanManager.getBeans(ofType, qualifiers);
            for(Bean<?> bean : beans) {
                try {
                    @SuppressWarnings("unchecked")
                    T object = (T) beanManager.getReference(bean, ofType, beanManager.createCreationalContext(bean));
                    setOfBeans.add(object);
                } catch (RuntimeException e) {
                    continue;
                }
            }
            return setOfBeans;
        } catch (IllegalArgumentException e) {
            return setOfBeans;
        }
        
    }



   



   
	
	
}
