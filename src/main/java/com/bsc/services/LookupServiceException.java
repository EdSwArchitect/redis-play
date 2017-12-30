package com.bsc.services;

/**
 *
 */
public class LookupServiceException extends Exception {
    /**
     *
      * @param message
     */
    public LookupServiceException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public LookupServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param cause
     */
    public LookupServiceException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    protected LookupServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
