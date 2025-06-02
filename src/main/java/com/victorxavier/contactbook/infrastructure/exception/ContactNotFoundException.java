package com.victorxavier.contactbook.infrastructure.exception;

public class ContactNotFoundException extends RuntimeException{

    public ContactNotFoundException(String msg){
        super(msg);
    }
    public ContactNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}
