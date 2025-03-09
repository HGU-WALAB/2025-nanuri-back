package com.walab.nanuri.commons.exception;

public class DoNotExistException extends RuntimeException {
    public DoNotExistException(String message) {
        super(message);
    }
}