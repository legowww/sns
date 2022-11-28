package com.example.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"), //409
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"), //404
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"), //401
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"); //500




    //한 enum 에러코드가 가지는 필드 2개
    private HttpStatus status; //에러코드에 따라 status 변경 가능하도록
    private String message; //에러코드에 따라 message 변경 가능하도록
}
