package com.tang.project.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
//@Slf4j
public class RestControllerExceptionHandler {

    /**
     * 服务器错误状态码
     */
    private final static int GENERIC_SERVER_ERROR_CODE = 2000;

    /**
     * 服务器错误异常提示语
     */
    private final static String GENERIC_SERVER_ERROR_MESSAGE = "服务器忙，请稍后再试";

//    @ExceptionHandler
//    public APIResponse handle(HttpServletRequest req, HandlerMethod method, Exception ex) {
//        if (ex instanceof BusinessException) {
//            BusinessException exception = (BusinessException) ex;
//            log.warn(String.format("访问 %s -> %s 出现业务异常！", req.getRequestURI(), method.toString()), ex);
//            return new APIResponse(false, null, exception.getCode(), exception.getMessage());
//        } else {
//            log.error(String.format("访问 %s -> %s 出现系统异常！", req.getRequestURI(), method.toString()), ex);
//            return new APIResponse(false, null, GENERIC_SERVER_ERROR_CODE, GENERIC_SERVER_ERROR_MESSAGE);
//        }
//    }
}