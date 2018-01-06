package com.bsc.services.ehcache;

import com.bsc.services.LookupListener;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerObject implements CacheEventListener {

    private Logger log = LoggerFactory.getLogger(ListenerObject.class);
    private String name;
    private LookupListener topListener;

    private ListenerObject() {

    }

    /**
     *
     * @param name
     */
    public ListenerObject(String name) {
       this(name, null);
    }

    /**
     *
     * @param name
     */
    public ListenerObject(String name, LookupListener topListener) {
        this.topListener = topListener;
        this.name = name;
    }

    /**
     * Invoked on {@link CacheEvent CacheEvent} firing.
     * <p>
     * This method is invoked according to the {@link EventOrdering}, {@link EventFiring} and
     * {@link EventType} requirements provided at listener registration time.
     * <p>
     * Any exception thrown from this listener will be swallowed and logged but will not prevent other listeners to run.
     *
     * @param event the actual {@code CacheEvent}
     */
    @Override
    public void onEvent(CacheEvent event) {
        Object key = event.getKey();

        log.info("Cache {} event fired: {}  for key: {}", name, event.getType(), event.getKey());

        if (topListener != null) {
            switch(event.getType()) {
                case CREATED:
                    topListener.keyChange(LookupListener.LISTENER_EVENT.CREATED, key.toString());
                    break;
                case EVICTED:
                    topListener.keyChange(LookupListener.LISTENER_EVENT.EVICTED, key.toString());
                    break;
                case EXPIRED:
                    topListener.keyChange(LookupListener.LISTENER_EVENT.EXPIRED, key.toString());
                    break;
                case REMOVED:
                    topListener.keyChange(LookupListener.LISTENER_EVENT.REMOVED, key.toString());
                    break;
                case UPDATED:
                    topListener.keyChange(LookupListener.LISTENER_EVENT.UPDATED, key.toString());
                    break;
            }

        }
    }
}
