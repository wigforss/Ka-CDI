package org.kasource.commons.cdi.extension.beanlisten;

import java.lang.reflect.Method;

import org.kasource.commons.cdi.extension.beanlisten.annotation.OnBeanDispose;
import org.kasource.commons.cdi.extension.beanlisten.annotation.OnBeanInjection;
import org.kasource.commons.cdi.extension.beanlisten.annotation.OnBeanPostConstruct;
import org.kasource.commons.cdi.extension.beanlisten.annotation.OnBeanPreDestroy;
import org.kasource.commons.reflection.ClassIntrospector;
import org.kasource.commons.reflection.ClassIntrospectorImpl;
import org.kasource.commons.reflection.filter.MethodFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanMethodListener implements BeanEventListener{
    private static final Logger LOG = LoggerFactory.getLogger(BeanMethodListener.class);
    private Object target;
    private Method postConstructMethod;
    private Method injectionMethod;
    private Method preDestroyMethod;
    private Method disposeMethod;
    
    public BeanMethodListener(Object target) {
        this.target = target;
        initialize();
    }
    
    private void initialize() {
        ClassIntrospector classIntrospectorImpl = new ClassIntrospectorImpl(target.getClass());
        postConstructMethod = classIntrospectorImpl.getMethod(new MethodFilterBuilder()
                                                            .annotated(OnBeanPostConstruct.class)
                                                            .returnType(Void.TYPE)
                                                            .isPublic()
                                                            .hasSignature(Object.class)
                                                            .build());
        injectionMethod = classIntrospectorImpl.getMethod(new MethodFilterBuilder()
                                                            .annotated(OnBeanInjection.class)
                                                            .returnType(Void.TYPE)
                                                            .isPublic()
                                                            .hasSignature(Object.class)
                                                            .build());
        preDestroyMethod = classIntrospectorImpl.getMethod(new MethodFilterBuilder()
                                                            .annotated(OnBeanPreDestroy.class)
                                                            .returnType(Void.TYPE)
                                                            .isPublic()
                                                            .hasSignature(Object.class)
                                                            .build());
        disposeMethod = classIntrospectorImpl.getMethod(new MethodFilterBuilder()
                                                            .annotated(OnBeanDispose.class)
                                                            .returnType(Void.TYPE)
        .isPublic()
        .hasSignature(Object.class)
        .build());
    }
    
    @Override
    public void onBeanEvent(BeanEvent event) {
        try {
            switch (event.getEventType()) {
            case POST_CONSTRUCT:
                if (postConstructMethod != null) {
                    postConstructMethod.invoke(target, event.getSource());
                }
                break;
           case INJECTED:
               if (injectionMethod != null) {
                   injectionMethod.invoke(target, event.getSource());
               }
                break;
            case PRE_DESTROY:
               if (preDestroyMethod != null) {
                   preDestroyMethod.invoke(target, event.getSource());
               }
                break;
            case DISPOSE:
               if (disposeMethod != null) {
                   disposeMethod.invoke(target, event.getSource());
               }
                break;
            }
        } catch (Exception e) {
            LOG.warn("Could not invoke event listener " + target + " on " + event.getEventType(), e);
        }
    }

}
