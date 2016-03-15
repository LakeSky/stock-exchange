package org.stockexchange.response;

import org.stockexchange.entity.Stock;

import java.io.Serializable;
import java.util.List;

/**
 * Response class for /listing web service call
 */
public class ListingResponse implements Serializable {
    private String status;   // Response status "Success" or error code
    private String message;  // Response message - used to provide details in error cases
    private List<Stock> listing; // Stock listing

    /**
     * default constructor
     */
    public ListingResponse(){

    }

    public List<Stock> getListing() {
        return listing;
    }

    public void setListing(List<Stock> listing) {
        this.listing = listing;
    }

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

    public ListingResponse(String status, String message, List<Stock> listing) {
        this.status = status;
        this.message = message;
        this.listing = listing;
    }
}
