package com.walab.nanuri.commons.exception.controller;


import com.walab.nanuri.commons.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionController {
    /**
     * 파일 업로드 중 오류가 발생했을 때 처리하는 예외 핸들러입니다.
     *
     * @param ex 발생한 {@link FileUploadException}
     * @return HTTP 500 상태 코드와 오류 메시지를 포함한 {@link ResponseEntity}
     */
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> handleFileUploadException(FileUploadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + ex.getMessage());
    }

    /**
     * Firebase Storage 관련 오류가 발생했을 때 처리하는 예외 핸들러입니다.
     *
     * @param ex 발생한 {@link FirebaseStorageException}
     * @return HTTP 500 상태 코드와 오류 메시지를 포함한 {@link ResponseEntity}
     */
    @ExceptionHandler(FirebaseStorageException.class)
    public ResponseEntity<String> handleFirebaseStorageException(FirebaseStorageException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Firebase Storage 오류: " + ex.getMessage());
    }

    /**
     * 파일 삭제 중 오류가 발생했을 때 처리하는 예외 핸들러입니다.
     *
     * @param ex 발생한 {@link FileDeletionException}
     * @return HTTP 500 상태 코드와 오류 메시지를 포함한 {@link ResponseEntity}
     */
    @ExceptionHandler(FileDeletionException.class)
    public ResponseEntity<String> handleFileDeletionException(FileDeletionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 삭제 실패: " + ex.getMessage());
    }

    /**
     * Firebase Storage에서 파일을 찾을 수 없는 경우 처리하는 예외 핸들러입니다.
     *
     * @param ex 발생한 {@link FileNotFoundException}
     * @return HTTP 404 상태 코드와 오류 메시지를 포함한 {@link ResponseEntity}
     */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일을 찾을 수 없음: " + ex.getMessage());
    }

    /**
     * 올바르지 않은 Firebase Storage URL이 제공된 경우 처리하는 예외 핸들러입니다.
     *
     * @param ex 발생한 {@link InvalidFileUrlException}
     * @return HTTP 400 상태 코드와 오류 메시지를 포함한 {@link ResponseEntity}
     */
    @ExceptionHandler(InvalidFileUrlException.class)
    public ResponseEntity<String> handleInvalidFileUrlException(InvalidFileUrlException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 Firebase Storage URL: " + ex.getMessage());
    }
}