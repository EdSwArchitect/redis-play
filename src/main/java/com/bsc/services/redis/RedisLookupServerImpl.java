package com.bsc.services.redis;

import com.bsc.services.LookupServer;
import com.bsc.services.LookupServiceException;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

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
    public void set(String table, String key, Map<String, Object> keyValues) throws LookupServiceException {

    }

    /**
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    @Override
    public Map<String, Object> getHash(String table, String key) throws LookupServiceException {
        return null;
    }

    /**
     * Disconnect
     */
    @Override
    public void disconnect() {
        connection.close();
    }

    @Override
    public String toString() {
        return "RedisLookupServerImpl{" +
                "client=" + client +
                ", connection=" + connection +
                '}';
    }
}
