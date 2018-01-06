package com.bsc.services.redis;

import com.bsc.services.LookupListener;
import com.bsc.services.LookupServer;
import com.bsc.services.LookupServiceException;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class RedisLookupServerImpl implements LookupServer {
    /** client */
    private RedisClient client;
    /** connection redis://localhost */
    private StatefulRedisConnection<String, String> connection;
    /** Redis commands */
    private RedisCommands<String, String>redisCommands;

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
        // no-op
    }

    /**
     * Connect to the Redis server
     * @param url The url for the lookup server
     * @throws LookupServiceException
     */
    @Override
    public void connect(String url) throws LookupServiceException {
        client = RedisClient.create(url);
        connection = client.connect();
        redisCommands = connection.sync();

        /*
        RedisURI redisURI = RedisURI.builder().
        RedisURI redisUri = RedisURI.Builder.redis("localhost")
                .withClientName("clientName")
                .withDatabase(1)
                .withHost("localhost")
                .withPort(0)
                .withTimeout(60, TimeUnit.SECONDS)
                .withPassword("authentication")
                .withDatabase(2)
                .build();
        */

    }

    /**
     * Get information about the connection
     *
     * @return Information about the connection
     */
    @Override
    public String getInfo() throws LookupServiceException {
        if (client == null || connection == null || redisCommands == null) {
            throw new LookupServiceException("LookupService to Redis not connected");
        }

        return redisCommands.info("server");
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
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());

        redisCommands.set(key, value);
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
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());

        long convTime = 0;

        switch(unit) {
            case DAYS:
                convTime = time *24 * 60 * 60;
                break;
            case HOURS:
                convTime = time * 60 * 60;
                break;
            case MINUTES:
                convTime = time * 60;
                break;
            case SECONDS:
                convTime = time;
                break;
            default:
        }

        redisCommands.setex(key, convTime,value);
    }

    /**
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    @Override
    public String get(String table, String key) throws LookupServiceException {
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());

        return redisCommands.get(key);
    }

    /**
     * @param table
     * @param key
     * @param keyValues
     * @throws LookupServiceException
     */
    @Override
    public void set(String table, String key, Map<String, String> keyValues) throws LookupServiceException {
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());


        redisCommands.hmset(key, keyValues);
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
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());

        long convTime = 0;

        switch(unit) {
            case DAYS:
                convTime = time *24 * 60 * 60;
                break;
            case HOURS:
                convTime = time * 60 * 60;
                break;
            case MINUTES:
                convTime = time * 60;
                break;
            case SECONDS:
                convTime = time;
                break;
            default:
        }

        redisCommands.hmset(key, keyValues);
        redisCommands.expire(key, convTime);

    }

    /**
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    @Override
    public Map<String, String> getHash(String table, String key) throws LookupServiceException {
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());

        return redisCommands.hgetall(key);
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
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());
        return redisCommands.keys("*");
    }


    /**
     * @param table
     * @param key
     * @throws LookupServiceException
     */
    @Override
    public void delete(String table, String key) throws LookupServiceException {
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());
        redisCommands.del(key);
    }

    /**
     * @param table
     * @param keys
     * @throws LookupServiceException
     */
    @Override
    public void delete(String table, String... keys) throws LookupServiceException {
        redisCommands.select(RedisTables.LOOKUP_TBL.valueOf(table).ordinal());
        redisCommands.del(keys);
    }

    /**
     * Watch changes in the table
     *
     * @param table The table name
     * @throws LookupServiceException Thrown if there is an error
     */
    @Override
    public void watch(String table, LookupListener listener) throws LookupServiceException {
        throw new LookupServiceException("Redis only allows a watch on transactions. Not implemented.");
    }

    /**
     * Stop  watching changes in the table
     *
     * @param table The table name
     * @throws LookupServiceException
     */
    @Override
    public void unwatch(String table) throws LookupServiceException {
        throw new LookupServiceException("Redis only allows a watch on transactions. Not implemented.");
    }

    /**
     * Disconnect
     */
    @Override
    public void disconnect() {
        connection.close();
        connection = null;
    }

    @Override
    public String toString() {
        return "RedisLookupServerImpl{" +
                "client=" + client +
                ", connection=" + connection +
                '}';
    }
}
