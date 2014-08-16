package org.kasource.commons.cdi.extension;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import org.kasource.commons.cdi.extension.beanlisten.BeanEvent;
import org.kasource.commons.cdi.extension.beanlisten.BeanEventListener;
import org.kasource.commons.cdi.extension.eager.Eager;

public class KaCommonsExtension implements Extension, BeanEventListener, Runnable {
    private Queue<EagerBean> eagerBeans = new LinkedList<EagerBean>();
    private volatile BeanEventListener beanListener;
    private Queue<BeanEvent> events = new LinkedList<BeanEvent>();
  
        
    <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> event) {
       event.setInjectionTarget(new KaCommonsListenerInjectionTarget<T>(event.getInjectionTarget(), this));
    }
    
    
    
    <T> void processBean(@Observes ProcessBean<T> event) {
        if (event.getAnnotated().isAnnotationPresent(Eager.class)
                    && event.getAnnotated().isAnnotationPresent(ApplicationScoped.class)) {
                    eagerBeans.add(new EagerBean(event.getBean(), event.getAnnotated().getBaseType()));
                }
       
    }
    

    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        while (!eagerBeans.isEmpty()) {
            EagerBean eagerBean = eagerBeans.poll();
            beanManager.getReference(eagerBean.getBean(), eagerBean.getBeanType(), beanManager.createCreationalContext(eagerBean.getBean())).toString();
        }
    }
    
    private static class EagerBean {
        private Bean<?> bean;
        private Type beanType;
        
        public EagerBean(Bean<?> bean, Type beanType) {
            this.bean = bean;
            this.beanType = beanType;
        }

        /**
         * @return the bean
         */
        public Bean<?> getBean() {
            return bean;
        }

        /**
         * @return the beanClass
         */
        public Type getBeanType() {
            return beanType;
        }
        
        
    }

    
   
    
    public void setBeanListener(BeanEventListener beanListener) {
        this.beanListener = beanListener;
        new Thread(this).start();
     }
     
     public void run() {
         while (!events.isEmpty()) {
             beanListener.onBeanEvent(events.poll());
         }
     }
    
   



    @Override
    public void onBeanEvent(BeanEvent event) {
        if (beanListener !=  null) {
            beanListener.onBeanEvent(event);
        } else {
            events.add(event);
        }
         
        
    }
    
}
