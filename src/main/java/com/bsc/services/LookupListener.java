package com.bsc.services;

public interface LookupListener {
    public enum LISTENER_EVENT {
        CREATED, UPDATED, REMOVED, EVICTED, EXPIRED
    };

    /**
     *
     *
     * @param event
     * @param key
     */
    public void keyChange(LISTENER_EVENT event, String key);
}
