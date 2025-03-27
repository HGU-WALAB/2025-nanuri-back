package com.walab.nanuri.commons.exception.controller;

import com.walab.nanuri.commons.exception.ItemNotExistException;
import com.walab.nanuri.commons.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ItemExceptionController {

    @ExceptionHandler(ItemNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleItemNotExistException(ItemNotExistException e) {
        ExceptionResponse response =
                ExceptionResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(e.getMessage())
                        .build();

        return ResponseEntity.badRequest().body(response);
    }
}
