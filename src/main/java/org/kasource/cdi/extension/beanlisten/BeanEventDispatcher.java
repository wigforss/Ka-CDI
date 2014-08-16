package org.kasource.cdi.extension.beanlisten;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kasource.cdi.extension.KaCommonsExtension;
import org.kasource.cdi.extension.eager.Eager;

@ApplicationScoped @Eager
public class BeanEventDispatcher implements BeanEventListener {
  
    @Inject
    private KaCommonsExtension kaCommonsExtension;

    private LinkedList<BeanEvent> events = new LinkedList<BeanEvent>();
    private Set<BeanEventListener> listeners = new HashSet<BeanEventListener>();
    
    @PostConstruct
    public void postConstruct() {
        kaCommonsExtension.setBeanListener(this);
    }

    @Override
    public void onBeanEvent(BeanEvent event) {
        events.add(event);
        for (BeanEventListener listener : listeners) {
            listener.onBeanEvent(event);
        }
    }

    public void addBeanListener(Object object) {
        BeanEventListener listener = null;
        if (object instanceof BeanEventListener) {
            listener = (BeanEventListener) object;
        } else {
            listener = new BeanMethodListener(object);
        }
        LinkedList<BeanEvent> currentEvents = new LinkedList<BeanEvent>();
        currentEvents.addAll(events);
        new Thread(new DispatcherThread(currentEvents, listener)).start();
        listeners.add(listener);
    }
  
    private static final class DispatcherThread implements Runnable {
        private Queue<BeanEvent> events;
        private BeanEventListener listener;
        
        private DispatcherThread(Queue<BeanEvent> events, BeanEventListener listener) {
            this.listener = listener;
            this.events = events;
        }

        @Override
        public void run() {
            while (!events.isEmpty()) {
                listener.onBeanEvent(events.poll());                
            }        
        }
    }
}
