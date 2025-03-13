package com.walab.nanuri.commons.exception;


import lombok.Getter;

@Getter
public class FirebaseStorageException extends RuntimeException {
    private final Integer status;

    public FirebaseStorageException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
