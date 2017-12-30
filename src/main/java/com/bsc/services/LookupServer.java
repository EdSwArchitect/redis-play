package com.bsc.services;

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
    public void set(String table, String key, Map<String, Object> keyValues) throws LookupServiceException;

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
    public Map<String, Object> getHash(String table, String key) throws LookupServiceException;

}
