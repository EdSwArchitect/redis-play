package com.bsc.services.ehcache;

import com.bsc.services.LookupServer;
import com.bsc.services.LookupServiceException;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.CacheManagerConfiguration;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


import static org.ehcache.config.builders.ResourcePoolsBuilder.newResourcePoolsBuilder;

public class EhcacheLookupServerImpl implements LookupServer {

    /** heap size */
    public static final String HEAP_SIZE = "heap.size";
    /** heap units */
    public static final String HEAP_UNITS = "heap.units";
    /** off heap size */
    public static final String OFF_HEAP_SIZE = "offHeap.size";
    /** off heap units */
    public static final String OFF_HEAP_UNITS = "offHeap.units";
    /** disk size */
    public static final String DISK_SIZE = "disk.size";
    /** disk units */
    public static final String DISK_UNITS = "disk.units";
    /** disk directory */
    public static final String DISK_DIR = "disk.directory";
    /** persistent on disk */
    public static final String DISK_PERSISTENT = "disk.persistence";
    /** cache name */
    public static final String CACHE_NAME = "cache.name";
    /** time to live */
    public static final String TTL_TIME = "ttl.time";
    /** ttl units */
    public static final String TTL_UNITS = "ttl.units";
    /** log */
    private Logger log = LoggerFactory.getLogger(EhcacheLookupServerImpl.class);

    /**
     * cache manager
     */
    private CacheManager cm;
    /** cache name */
    private String cacheName;
    private HashMap<String, HashSet<String>> cacheKeys;



    /**
     *
     */
    public EhcacheLookupServerImpl() {
//        cm = CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("expires",
//                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
//                            newResourcePoolsBuilder().heap(500, EntryUnit.ENTRIES)).
//                            withExpiry(Expirations.timeToLiveExpiration(Duration.of(45, TimeUnit.SECONDS))
//                            ))
//                .build();
//        cm.init();
//
//        cacheKeys = new HashMap<String, HashSet<String>>();

    }


    /**
     * Default initialization
     */
    @Override
    public void init() {
        // no-op
    }

    /**
     * Initialization with parameters
     *
     * @param params
     */
    @Override
    public void init(Map<String, Object> params) {
        String l = String.format("The params are: %s", params);
        log.info(l);

        cacheName = (String)params.get(CACHE_NAME);

        CacheManagerBuilder cmb = CacheManagerBuilder.newCacheManagerBuilder();
        CacheManagerConfiguration<PersistentCacheManager> cmc = null;

        CacheConfigurationBuilder ccb = null;
        ResourcePoolsBuilder rpb = ResourcePoolsBuilder.newResourcePoolsBuilder();

        if (params.containsKey(HEAP_SIZE)) {
            long size = (long)params.get(HEAP_SIZE);
            ResourceUnit unit = getUnit(params, HEAP_UNITS, EntryUnit.ENTRIES);

            rpb = rpb.heap(size, unit);
        }
        if (params.containsKey(OFF_HEAP_SIZE)) {
            long size = (long)params.get(OFF_HEAP_SIZE);
            ResourceUnit unit = getUnit(params, OFF_HEAP_UNITS, EntryUnit.ENTRIES);

            rpb = rpb.offheap(size, (MemoryUnit)unit);
        }

        if (params.containsKey(DISK_SIZE)) {
            long size = (long)params.get(DISK_SIZE);
            ResourceUnit unit = getUnit(params, DISK_UNITS, MemoryUnit.MB);

            boolean persisted = false;

            if (params.containsKey(DISK_PERSISTENT)) {
                persisted = (Boolean)params.get(DISK_PERSISTENT);
            }

            rpb = rpb.disk(size, (MemoryUnit)unit, persisted);

            if (persisted) {

                if (params.containsKey(DISK_DIR)) {
                    cmc = CacheManagerBuilder.persistence(new File((String)params.get(DISK_DIR)));

                    cmb = cmb.with(cmc);
                } // if (params.containsKey(DISK_DIR)) {
            } // if (persisted) {


        } // if (params.containsKey(DISK_SIZE)) {

        //withExpiry(Expirations.timeToLiveExpiration(Duration.of(45, TimeUnit.SECONDS))
        if (params.containsKey(TTL_TIME)) {
            long ttl = (Long)params.get(TTL_TIME);
            TimeUnit tu = TimeUnit.HOURS;

            if (params.containsKey(TTL_UNITS)) {
                String ttlUnits = ((String)params.get(TTL_UNITS)).toLowerCase();

                if (ttlUnits.startsWith("sec")) {
                    tu = TimeUnit.SECONDS;
                }
                else if (ttlUnits.startsWith("min")) {
                    tu = TimeUnit.MINUTES;
                }
                else {
                    tu = TimeUnit.HOURS;
                }
            } // if (params.containsKey(TTL_UNITS)) {

            ccb = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, rpb).
                    withExpiry(Expirations.timeToLiveExpiration(Duration.of(ttl, tu)));
        }
        else {
            ccb = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, rpb);
        }

        cmb = cmb.withCache(cacheName, ccb);

        cm = cmb.build();

        l = String.format("CacheManagerBuilder: %s", cm);
        log.info(l);

        cm.init();

        // no-op
    }


    /**
     * Connect to the lookup server
     *
     * @param url The url for the lookup server
     * @throws LookupServiceException Thrown when connection error occurs
     */
    @Override
    public void connect(String url) throws LookupServiceException {
    }

    /**
     * Disconnect from the server
     */
    @Override
    public void disconnect() {
        cm.close();
    }

    /**
     * Get information about the connection
     *
     * @return Information about the connection
     * @throws LookupServiceException Thrown when connection error occurs
     */
    @Override
    public String getInfo() throws LookupServiceException {
        return cacheName;
    }

    /**
     * Set the key/value pair for the table
     *
     * @param table The table in the lookup server
     * @param key   The key
     * @param value The value
     * @throws LookupServiceException Thrown on lookup service error
     */
    @Override
    public void set(String table, String key, String value) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        cache.put(key, value);

    }

    /**
     * Set the key/value pair for the table
     *
     * @param table The table in the lookup server
     * @param key   The key
     * @param value The value
     * @param time
     * @param unit
     * @throws LookupServiceException
     */
    @Override
    public void set(String table, String key, String value, long time, TimeUnit unit) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        cache.put(key, value);

    }

    /**
     * @param table
     * @param key
     * @param keyValues
     * @throws LookupServiceException
     */
    @Override
    public void set(String table, String key, Map<String, String> keyValues) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        StringBuilder sb = new StringBuilder("{\n");

        for (String k : keyValues.keySet()) {
            sb.append("\"").append(k).append("\" : \"").append(keyValues.get(k));
            sb.append("\",\n");

        }
        sb.setLength(sb.length()-2);
        sb.append("}");

        cache.put(key, sb.toString());

    }

    /**
     * @param table
     * @param key
     * @param keyValues
     * @param time
     * @param unit
     * @throws LookupServiceException
     */
    @Override
    public void set(String table, String key, Map<String, String> keyValues, long time, TimeUnit unit) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        StringBuilder sb = new StringBuilder("{\n");

        for (String k : keyValues.keySet()) {
            sb.append("\"").append(k).append("\" : \"").append(keyValues.get(k));
            sb.append("\",\n");

        }
        sb.setLength(sb.length()-2);
        sb.append("}");

        cache.put(key, sb.toString());
    }

    /**
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    @Override
    public String get(String table, String key) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        return cache.get(key);
    }

    /**
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    @Override
    public Map<String, String> getHash(String table, String key) throws LookupServiceException {
        return null;
    }

    /**
     * Get the list of keys from the table
     *
     * @param table
     * @return
     * @throws LookupServiceException
     */
    @Override
    public List<String> getKeys(String table) throws LookupServiceException {
        throw new LookupServiceException("Ehcache doesn't implement a means of retrieving all keys");
    }

    /**
     * @param table
     * @param key
     * @throws LookupServiceException
     */
    @Override
    public void delete(String table, String key) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        cache.remove(key);
    }

    /**
     * @param table
     * @param keys
     * @throws LookupServiceException
     */
    @Override
    public void delete(String table, String... keys) throws LookupServiceException {
        Cache<String, String> cache =
                cm.getCache(table, String.class, String.class);

        Set<String> keyset = new HashSet<String>();

        for (String k : keys) {
            keyset.add(k);
        }

        cache.removeAll(keyset);
    }

    /**
     * Watch changes in the table
     *
     * @param table The table name
     * @throws LookupServiceException Thrown if there is an error
     */
    @Override
    public void watch(String table) throws LookupServiceException {
        // definitely can be implemented. depends on cache configuration
    }

    /**
     * Stop  watching changes in the table
     *
     * @param table The table name
     * @throws LookupServiceException
     */
    @Override
    public void unwatch(String table) throws LookupServiceException {

    }

    /**
     *
     * @param params
     * @param paramUnit
     * @return
     */
    private ResourceUnit getUnit(Map<String, Object> params, String paramUnit, ResourceUnit defaultUnit) {
        ResourceUnit unit;

        String v = (String)params.get(paramUnit);

        if (v != null) {
            v = v.toLowerCase();

            if (v.startsWith("mb")) {
                unit = MemoryUnit.MB;
            } // if (v.startsWith("mb")) {
            else if (v.startsWith("gb")) {
                unit = MemoryUnit.GB;
            } // else if (v.startsWith("gb")) {
            else {
                unit = defaultUnit;
            }
        } // if (v != null) {
        else {
            unit = defaultUnit;
        }

        return unit;
    }

    @Override
    public String toString() {
        return "EhcacheLookupServerImpl{" +
                "cm=" + cm +
                ", cacheName='" + cacheName + '\'' +
                ", cacheKeys=" + cacheKeys +
                '}';
    }
}
