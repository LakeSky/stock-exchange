package org.stockexchange.response;

import org.stockexchange.entity.Ticker;

import java.io.Serializable;

/**
 * Response class for /listing web service call
 */


public class WatchResponse implements Serializable {
    private String status;   // Response status "Success" or error code
    private String message;  // Response message - used to provide details in error cases

    private Ticker ticker;   // the Ticker object to be returned by the service

    public WatchResponse() {
    }


    public WatchResponse(String status, String message, Ticker ticker) {
        this.status = status;
        this.message = message;
        this.ticker = ticker;
    }

    /**
     * Constructor used for returning error messages with empty ticker
     * @param status
     * @param message
     */
    public WatchResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.ticker = new Ticker();
    }

    /**
     * Default constructor
     * @return
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}
