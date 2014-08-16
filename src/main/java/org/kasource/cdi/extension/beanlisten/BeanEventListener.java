package org.kasource.cdi.extension.beanlisten;

import java.util.EventListener;

public interface BeanEventListener extends EventListener {
    public void onBeanEvent(BeanEvent event);
}
