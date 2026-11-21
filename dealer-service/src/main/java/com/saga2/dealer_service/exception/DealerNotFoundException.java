package com.saga2.dealer_service.exception;

public class DealerNotFoundException extends RuntimeException{

    public DealerNotFoundException(String message){
        super(message);
    }
}
