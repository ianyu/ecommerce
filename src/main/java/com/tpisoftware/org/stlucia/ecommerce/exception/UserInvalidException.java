package com.tpisoftware.org.stlucia.ecommerce.exception;

public class UserInvalidException extends RuntimeException {

    public UserInvalidException() {
        super("Invalid User");
    }
}
