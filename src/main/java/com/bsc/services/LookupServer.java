package com.bsc.services;

import java.util.List;
import java.util.Map;

public interface LookupServer {
    /**
     * Connect to the lookup server
     * @param url The url for the lookup server
     * @throws LookupServiceException Thrown when connection error occurs
     */
    public void connect(String url) throws LookupServiceException;

    /**
     * Disconnect from the server
     */
    public void disconnect();

    /**
     * Get information about the connection
     * @return Information about the connection
     * @throws LookupServiceException Thrown when connection error occurs
     */
    public String getInfo() throws LookupServiceException ;

    /**
     * Set the key/value pair for the table
     * @param table The table in the lookup server
     * @param key The key
     * @param value The value
     * @throws LookupServiceException Thrown on lookup service error
     */
    public void set(String table, String key, String value) throws LookupServiceException;

    /**
     *
     * @param table
     * @param key
     * @param keyValues
     * @throws LookupServiceException
     */
    public void set(String table, String key, Map<String, String> keyValues) throws LookupServiceException;

    /**
     *
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    public String get(String table, String key) throws LookupServiceException;

    /**
     *
     * @param table
     * @param key
     * @return
     * @throws LookupServiceException
     */
    public Map<String, String> getHash(String table, String key) throws LookupServiceException;

    /**
     * Get the list of keys from the table
     * @param table
     * @return
     * @throws LookupServiceException
     */
    public List<String> getKeys(String table) throws LookupServiceException;

    /**
     *
     * @param table
     * @param key
     * @throws LookupServiceException
     */
    public void delete(String table, String key) throws LookupServiceException;

    /**
     *
     * @param table
     * @param keys
     * @throws LookupServiceException
     */
    public void delete(String table, String... keys) throws LookupServiceException;

    /**
     * Watch changes in the table
     * @param table The table name
     * @throws LookupServiceException Thrown if there is an error
     */
    public void watch(String table) throws LookupServiceException;

    /**
     * Stop  watching changes in the table
     * @param table The table name
     * @throws LookupServiceException
     */
    public void unwatch(String table) throws LookupServiceException;
}
