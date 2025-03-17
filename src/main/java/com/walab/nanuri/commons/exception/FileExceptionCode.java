package com.walab.nanuri.commons.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileExceptionCode {
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_COUNT_UPPER(HttpStatus.BAD_REQUEST,"파일이 5개를 넘어갔습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 제한을 초과했습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다."),
    FILE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "최대 업로드 가능한 파일 개수를 초과했습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "업로드된 파일이 비어 있습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "파일명이 유효하지 않습니다."),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 읽는 중 오류가 발생했습니다."),

    FILE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
    DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "디렉토리 생성에 실패했습니다."),

    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다."),

    FILE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "파일에 접근할 권한이 없습니다."),
    FILE_PATH_INVALID(HttpStatus.FORBIDDEN, "파일 경로가 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;
}
