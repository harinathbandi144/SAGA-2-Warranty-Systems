package com.saga2.dealer_service.exception;

public class DealerAlreadyExistsException extends RuntimeException{
    public DealerAlreadyExistsException(String message){
        super(message);
    }
}
