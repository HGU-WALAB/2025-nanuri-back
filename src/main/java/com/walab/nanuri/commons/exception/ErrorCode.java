package com.walab.nanuri.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    MISSING_REGISTER_BAD_REQUEST(BAD_REQUEST, "등록자에 대한 정보가 입력되지 않았습니다."),
    MISSING_FILE_BAD_REQUEST(BAD_REQUEST,"요청에 대한 이미지 파일이 누락되었습니다."),
    MISSING_REFRESH_TOKEN(BAD_REQUEST,"refresh token이 존재하지 않습니다."),
    MISSING_WISH(BAD_REQUEST,"해당 wish가 존재하지 않습니다."),
    MISSING_WANT_POST(BAD_REQUEST, "해당 post가 존재하지 않습니다"),
    CANNOT_APPLY_OWN_POST(BAD_REQUEST, "본인의 게시글에는 신청할 수 없습니다."),
    ALREADY_FINISHED_POST(BAD_REQUEST, "이미 마감된 게시글입니다."),
    INVALID_SHARE_STATUS(BAD_REQUEST, "유효하지 않는 공유 상태입니다."),

    /* 403 FORBIDDEN : 접근 권한 제한 */
    /* Valid : 유효한 */
    VALID_USER(FORBIDDEN, "해당 정보에 접근 권한이 존재하지 않습니다."),
    VALID_ITEM(FORBIDDEN, "해당 아이템에 접근 권한이 존재하지 않습니다."),
    VALID_DELETE_CHATROOM(FORBIDDEN, "해당 채팅방을 삭제할 권한이 없습니다."),
    VALID_OWN_ITEM(FORBIDDEN, "본인의 물건은 나눔 신청할 수 없습니다."),
    VALID_ALREADY_APPLIED_POST(FORBIDDEN, "이미 신청한 게시글입니다."),
    VALID_TOKEN(FORBIDDEN, "만료된 토큰입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 회원 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(NOT_FOUND,"해당 이미지를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(NOT_FOUND,"해당 아이템을 찾을 수 없습니다."),
    HISTORY_NOT_FOUND(NOT_FOUND, "해당 히스토리를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(NOT_FOUND,"해당 카테고리를 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    WANT_POST_NOT_FOUND(NOT_FOUND, "해당 포스트를 찾을 수 없습니다."),
    TOKEN_NOT_FOUND(NOT_FOUND,"존재하지 않는 토큰입니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    /* DUPLICATE : (다른 무엇과) 똑같은 */
    DUPLICATE_EMAIL(CONFLICT, "이메일이 이미 존재합니다."),
    DUPLICATE_DIFFERENT_USER(CONFLICT, "해당 글의 작성자와 정보가 일치하지 않습니다."),
    DUPLICATE_APPLICATION_ITEM(CONFLICT, "이미 나눔 신청한 물건입니다."),

    /* 500 : */
    UNSUCCESSFUL_HISNET_LOGIN(INTERNAL_SERVER_ERROR,"히즈넷 로그인에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}