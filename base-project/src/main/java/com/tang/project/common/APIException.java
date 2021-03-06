package com.tang.project.common;

import lombok.Getter;

/**
 * 统一返回的异常信息
 */
public class APIException extends RuntimeException {
    @Getter
    private int errorCode;
    @Getter
    private String errorMessage;

    public APIException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public APIException(Throwable cause, int errorCode, String errorMessage) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}