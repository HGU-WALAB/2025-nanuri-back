package com.walab.nanuri.commons.exception;

public class UnauthorizedDeletionException extends RuntimeException {
    public UnauthorizedDeletionException(String message) {
        super(message);
    }
}