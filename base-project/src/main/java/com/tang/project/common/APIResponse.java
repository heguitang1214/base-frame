package com.tang.project.common;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;


/**
 * 统一的响应包装体
 *
 * @param <T> 实体类型
 */
@Data
public class APIResponse<T> {

    /**
     * 响应码
     */
    private int code;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String message;

    public APIResponse() {
    }

    public APIResponse(int code, boolean success, T data, String message) {
        this.code = code;
        this.success = success;
        this.data = data;
        this.message = message;
    }
}