package com.bsc.services.ehcache;

import com.bsc.services.LookupServer;
import com.bsc.services.LookupServiceException;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.ehcache.config.builders.ResourcePoolsBuilder.newResourcePoolsBuilder;

public class EhcacheLookupServerImpl implements LookupServer {
    /**
     * cache manager
     */
    private CacheManager cm;
    private HashMap<String, HashSet<String>> cacheKeys;

    /**
     *
     */
    public EhcacheLookupServerImpl() {
        cm = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("expires",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                            newResourcePoolsBuilder().heap(500, EntryUnit.ENTRIES)).
                            withExpiry(Expirations.timeToLiveExpiration(Duration.of(45, TimeUnit.SECONDS))
                            ))
                .build();
        cm.init();

        cacheKeys = new HashMap<String, HashSet<String>>();

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
        return "";
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
}
