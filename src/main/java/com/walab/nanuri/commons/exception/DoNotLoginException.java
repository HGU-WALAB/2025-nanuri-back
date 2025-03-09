package com.walab.nanuri.commons.exception;

public class DoNotLoginException extends RuntimeException {
    private static final String MESSAGE = "로그인하지 않은 사용자입니다.";

    public DoNotLoginException() {
        super(MESSAGE);
    }
}