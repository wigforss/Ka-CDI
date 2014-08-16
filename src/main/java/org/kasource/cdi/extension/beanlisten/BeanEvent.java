package org.kasource.cdi.extension.beanlisten;

import java.util.EventObject;



public class BeanEvent extends EventObject {
  
    private static final long serialVersionUID = 1L;
    
    private BeanEventType eventType;
   
    public BeanEvent(Object instance, BeanEventType eventType) {
        super(instance);
        this.eventType = eventType;
    }

   
    /**
     * @return the eventType
     */
    public BeanEventType getEventType() {
        return eventType;
    }
}
