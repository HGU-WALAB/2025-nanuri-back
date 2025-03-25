package com.walab.nanuri.commons.exception;

public class WishNotExistException extends RuntimeException {
    private static final String MESSAGE = "존재하지 않는 Wish 입니다.";

    public WishNotExistException() {
        super(MESSAGE);
    }
}
