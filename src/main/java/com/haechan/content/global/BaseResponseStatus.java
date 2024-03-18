package com.haechan.content.global;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "Success!"),

    /**
     *  2000: Request 오류
     */
    // NULL_TOKEN(false, 2000, "토큰 값을 입력해주세요."),
    NULL_TOKEN(false, 2000, "Null Token!"),

    // 이미 존재하는 계약서
    INVALID_CONTRACT(false, 3001, "The Contract Already Exists!"),

    // 존재하지 않는 ost
    NON_EXIST_OST(false, 3003, "There's No Such OST!"),

    /**
     * 4000: DB, Server 오류
     */
    // 데이터베이스 연결에 실패
    DATABASE_ERROR(false, 4000, "DB connection fail!");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}