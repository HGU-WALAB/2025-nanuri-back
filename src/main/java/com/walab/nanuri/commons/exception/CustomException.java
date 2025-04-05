package com.walab.nanuri.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    // 기존 생성자에서 메시지를 포함하도록 수정
    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // 기존 생성자에서 에러코드만 받을 수 있는 생성자 유지
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
