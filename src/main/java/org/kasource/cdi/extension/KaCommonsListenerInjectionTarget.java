package org.kasource.cdi.extension;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.kasource.cdi.extension.beanlisten.BeanEvent;
import org.kasource.cdi.extension.beanlisten.BeanEventListener;
import org.kasource.cdi.extension.beanlisten.BeanEventType;

/**
 * InjectionTarget decorator that calls the registered BeanInjectionListeners.
 * 
 * @author rikardwi
 *
 * @param <T> Type of the bean handled.
 **/
public  class KaCommonsListenerInjectionTarget<T> implements InjectionTarget<T> {
   
   private BeanEventListener beanListener;
   private InjectionTarget<T> injectionTarget;

   /**
    * Constructor.
    * 
    * @param injectionTarget         The injection target.
    * @param beanInjectionListeners  The registered BeanInjectionListeners to invoke.
    **/
    public KaCommonsListenerInjectionTarget(InjectionTarget<T> injectionTarget, BeanEventListener beanListener) {
        this.injectionTarget = injectionTarget;
        this.beanListener = beanListener;
    }


    /**
     * Delegates to injectionTarget.
     **/
    @Override
    public void dispose(T instance) {
        injectionTarget.dispose(instance);
        beanListener.onBeanEvent(new BeanEvent(instance, BeanEventType.DISPOSE));
    }

    /**
     * Delegates to injectionTarget.
     **/
    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        
        return injectionTarget.getInjectionPoints();
    }

    /**
     * Delegates to injectionTarget.
     **/
    @Override
    public T produce(CreationalContext<T> ctx) {
        return injectionTarget.produce(ctx);
    }


    /**
     * Delegates to injectionTarget and calls inject on beanInjectionListeners.
     * 
     * @param instance which is injected.
     **/
    @Override
    public void inject(T instance, CreationalContext<T> ctx) {
        injectionTarget.inject(instance, ctx);
        beanListener.onBeanEvent(new BeanEvent(instance, BeanEventType.INJECTED));
    }

    /**
     * Delegates to injectionTarget and calls postConstruct on beanInjectionListeners.
     * 
     * @param instance which is created.
     **/
    @Override
    public void postConstruct(T instance) {
        injectionTarget.postConstruct(instance);        
        beanListener.onBeanEvent(new BeanEvent(instance, BeanEventType.POST_CONSTRUCT));
    }
    
    
    /**
     * Delegates to injectionTarget and calls preDestroy on beanInjectionListeners.
     * 
     * @param instance which is about to be destroyed.
     **/
    @Override
    public void preDestroy(T instance) {
        injectionTarget.preDestroy(instance);
        beanListener.onBeanEvent(new BeanEvent(instance, BeanEventType.PRE_DESTROY));
    }
}
