package com.walab.nanuri.commons.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileCommonException extends RuntimeException {
    private final FileExceptionCode fileExceptionCode;
    public FileCommonException(FileExceptionCode fileExceptionCode) {
        super(fileExceptionCode.getMessage());
        this.fileExceptionCode = fileExceptionCode;
    }
}
