package com.bsc.services;

import com.bsc.services.redis.RedisLookupServerImpl;

public class LookupServerFactory {
    /**
     * Get the default lookup server implementation
     *
     * @return Returns the lookup service implementation
     * @throws LookupServiceException Thrown if you can't get the implementation
     */
    public static LookupServer getLookupServer() throws LookupServiceException {
        LookupServer server = new RedisLookupServerImpl();

        return server;
    }

    /**
     *
     * @param impl
     * @return Returns the lookup service implementation
     * @throws LookupServiceException Thrown if you can't get the implementation
     */
    public static LookupServer getLookupServer(String impl) throws LookupServiceException {
        LookupServer server = null;

        return server;

    }
}
