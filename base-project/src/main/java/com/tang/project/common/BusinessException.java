package com.tang.project.common;

import lombok.Getter;

public class BusinessException extends RuntimeException {
    @Getter
    private int errorCode;
    @Getter
    private String errorMessage;

    public BusinessException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessException(Throwable cause, int errorCode, String errorMessage) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}