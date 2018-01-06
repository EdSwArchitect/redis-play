package com.bsc.services.ehcache;

import com.bsc.services.LookupListener;
import com.bsc.services.LookupServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class EhcacheLookupServerImplTest {
    private Logger log = LoggerFactory.getLogger(EhcacheLookupServerImplTest.class);

    public class Listener implements LookupListener {

        /**
         * @param event
         * @param key
         */
        @Override
        public void keyChange(LISTENER_EVENT event, String key) {
            log.info("KeyChange: {} for key {}", event, key);
        }
    }

    @Test
    public void getPutTest() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EhcacheLookupServerImpl.HEAP_SIZE, 10L);
        params.put(EhcacheLookupServerImpl.HEAP_UNITS, "entries");
        params.put(EhcacheLookupServerImpl.CACHE_NAME, "edwin_cache");

        EhcacheLookupServerImpl cache = new EhcacheLookupServerImpl();
        cache.init(params);

        try {
            String info = cache.getInfo();

            log.info("Info: " + info);

            cache.set("edwin_cache", "key1", "valu1");
            cache.set("edwin_cache", "key2", "valu2");
            cache.set("edwin_cache", "key3", "valu3");

            log.info("Value for key1: " + cache.get("edwin_cache", "key1"));
            log.info("Value for key2: " + cache.get("edwin_cache", "key2"));
            log.info("Value for key3: " + cache.get("edwin_cache", "key3"));
            log.info("Value for goober: " + cache.get("edwin_cache", "goober"));
            log.info("Value for key1, but no cache: " + cache.get("no_cache", "key1"));

        } catch (LookupServiceException lse) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }


    }

    @Test
    public void expelTest() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EhcacheLookupServerImpl.HEAP_SIZE, 5L);
        params.put(EhcacheLookupServerImpl.HEAP_UNITS, "entries");
        params.put(EhcacheLookupServerImpl.CACHE_NAME, "edwin_cache");

        EhcacheLookupServerImpl cache = new EhcacheLookupServerImpl();
        cache.init(params);

        try {
            String info = cache.getInfo();

            log.info("Info: " + info);

            for (int i = 1; i <= 5; i++) {
                cache.set("edwin_cache", "key" + i, "valu" + i);
            }

            String line;

            for (int i = 1; i <= 5; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("edwin_cache", "key"+i));
                log.info(line);
            }

            for (int i = 6; i <= 10; i++) {
                cache.set("edwin_cache", "key" + i, "valu" + i);
            }

            for (int i = 1; i <= 10; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("edwin_cache", "key"+i));
                log.info(line);
            }
        } catch (LookupServiceException lse) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void expireTest()  {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EhcacheLookupServerImpl.HEAP_SIZE, 5L);
        params.put(EhcacheLookupServerImpl.HEAP_UNITS, "entries");
        params.put(EhcacheLookupServerImpl.CACHE_NAME, "edwin_cache");
        params.put(EhcacheLookupServerImpl.TTL_TIME, 45L);
        params.put(EhcacheLookupServerImpl.TTL_UNITS, "seconds");

        EhcacheLookupServerImpl cache = new EhcacheLookupServerImpl();
        cache.init(params);

        Listener lobj = new Listener();


        try {
            cache.watch("edwin_cache", lobj);
            String info = cache.getInfo();

            log.info("Info: " + info);

            for (int i = 1; i <= 5; i++) {
                cache.set("edwin_cache", "key" + i, "valu" + i);
            }

            String line;

            for (int i = 1; i <= 5; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("edwin_cache", "key"+i));
                log.info(line);
            }


            try {
                TimeUnit.SECONDS.sleep(60L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= 5; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("edwin_cache", "key"+i));
                log.info(line);
            }

        } catch (LookupServiceException lse) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void persistTest() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EhcacheLookupServerImpl.HEAP_SIZE, 5L);
        params.put(EhcacheLookupServerImpl.HEAP_UNITS, "entries");
        params.put(EhcacheLookupServerImpl.CACHE_NAME, "save_cache");
        params.put(EhcacheLookupServerImpl.DISK_PERSISTENT, true);
        params.put(EhcacheLookupServerImpl.DISK_DIR, "/home/edwin/cache");
        params.put(EhcacheLookupServerImpl.DISK_SIZE, 500L);
        params.put(EhcacheLookupServerImpl.DISK_UNITS, "mb");

        EhcacheLookupServerImpl cache = new EhcacheLookupServerImpl();
        cache.init(params);

        try {
            for (int i = 1; i <= 5; i++) {
                cache.set("save_cache", "key" + i, "valu" + i);
            }

            String line;

            for (int i = 1; i <= 5; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("save_cache", "key"+i));
                log.info(line);
            }

        } catch (LookupServiceException lse) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void persistTestRead() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put(EhcacheLookupServerImpl.HEAP_SIZE, 5L);
        params.put(EhcacheLookupServerImpl.HEAP_UNITS, "entries");
        params.put(EhcacheLookupServerImpl.CACHE_NAME, "save_cache");
        params.put(EhcacheLookupServerImpl.DISK_PERSISTENT, true);
        params.put(EhcacheLookupServerImpl.DISK_DIR, "/home/edwin/cache");
        params.put(EhcacheLookupServerImpl.DISK_SIZE, 500L);
        params.put(EhcacheLookupServerImpl.DISK_UNITS, "mb");

        EhcacheLookupServerImpl cache = new EhcacheLookupServerImpl();
        cache.init(params);

        try {
            for (int i = 1; i <= 5; i++) {
                cache.set("save_cache", "key" + i, "valu" + i);
            }

            String line;

            for (int i = 1; i <= 5; i++) {
                line = String.format("Value for key%d: '%s'", i, cache.get("save_cache", "key"+i));
                log.info(line);
            }

        } catch (LookupServiceException lse) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            lse.printStackTrace(pw);

            Assert.fail(sw.toString());
        }
    }

    @Test
    public void watch() {
    }
}