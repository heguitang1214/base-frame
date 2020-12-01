package com.tang.project.common;

import lombok.Data;


/**
 * 统一的响应包装体
 *
 * @param <T> 实体类型
 */
@Data
public class APIResponse<T> {
    private boolean success;
    private T data;
    private int code;
    private String message;
}