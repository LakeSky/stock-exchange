package org.stockexchange.exceptions;

import org.springframework.validation.Errors;

/**
 * Duplicate Stock Exception in input file
 */
public class DuplicateStockException extends RuntimeException{


    private static final long serialVersionUID = 1L;

    private Errors errors = null;

    public DuplicateStockException(String message){
        super(message);
    }

    public DuplicateStockException(String message, Errors e){
        super(message);
        this.errors = e;
    }

    public Errors getErrors() { return errors; }

}
