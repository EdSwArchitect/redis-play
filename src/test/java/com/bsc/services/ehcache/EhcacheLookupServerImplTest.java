package com.bsc.services.ehcache;

import com.bsc.services.LookupServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class EhcacheLookupServerImplTest {
    private Logger log = LoggerFactory.getLogger(EhcacheLookupServerImplTest.class);

    @Test
    public void init() {

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


        //        public static final String HEAP_UNITS = "heap.units";
//        public static final String OFF_HEAP_SIZE = "offHeap.size";
//        public static final String OFF_HEAP_UNITS = "offHeap.units";
//        public static final String DISK_SIZE = "disk.size";
//        public static final String DISK_UNITS = "disk.units";
//        public static final String DISK_DIR = "disk.directory";
//        public static final String DISK_PERSISTENT = "disk.persistence";
//        public static final String CACHE_NAME = "cache.name";
//        public static final String TTL_TIME = "ttl.time";
//        public static final String TTL_UNITS = "ttl.units";

    }

    @Test
    public void set() {
    }

    @Test
    public void get() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void delete1() {
    }

    @Test
    public void watch() {
    }
}