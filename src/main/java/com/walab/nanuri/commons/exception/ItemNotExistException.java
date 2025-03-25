package com.walab.nanuri.commons.exception;

public class ItemNotExistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 Item 입니다.";

    public ItemNotExistException() {
        super(MESSAGE);
    }
}
