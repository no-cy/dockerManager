package com.example.dockermanager.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResponseCode {
    // oauth
    OAUTH_DATA_GET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Oauth Login 값을 받아오는데 실패했습니다."),
    OAUTH_ACCESS_TOKEN_GET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Oauth Login 값을 받아오는데 실패했습니다."),
    OAUTH_USER_INFO_GET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Outh access token을 사용하여 사용자 정보를 받아오는데 실패했습니다."),

    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다."),
    USER_JOIN_SUCCESS(HttpStatus.OK, "회원가입에 성공했습니다."),

    USER_DUPLICATION_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    USER_DUPLICATION_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 email입니다."),

    VALID_CHECK_FAILED(HttpStatus.BAD_REQUEST, "유효성 검사에 실패했습니다.");

    HttpStatus httpStatus;
    String message;
}