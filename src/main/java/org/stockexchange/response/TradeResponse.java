package org.stockexchange.response;

import java.io.Serializable;

/**
 * Response class for /trade web service call
 */
public class TradeResponse implements Serializable {
    private String status;   // Response status "Success" or error code
    private String message;  // Response message - used to provide details in error cases

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

    public TradeResponse() {
    }

    public TradeResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
