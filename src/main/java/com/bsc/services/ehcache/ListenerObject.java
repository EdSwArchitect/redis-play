package com.bsc.services.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerObject implements CacheEventListener {

    private Logger log = LoggerFactory.getLogger(ListenerObject.class);
    private String name;

    private ListenerObject() {

    }

    /**
     *
     * @param name
     */
    public ListenerObject(String name) {
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

        log.info("Cache {} event fired: {}  for key: {}", name, event.getType(), event.getKey());
    }
}
