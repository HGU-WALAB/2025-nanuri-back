package com.walab.nanuri.commons.exception;

import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private final Integer status;

    public FileUploadException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
