package com.tang.project.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class APIResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 服务器错误状态码
     */
    private final static int GENERIC_SERVER_ERROR_CODE = 4000;

    /**
     * 服务器错误异常提示语
     */
    private final static String GENERIC_SERVER_ERROR_MESSAGE = "服务器忙，请稍后再试";

    /**
     * 自动处理APIException，包装为APIResponse
     *
     * @param request 请求
     * @param ex      异常
     * @return 返回
     */
    @ExceptionHandler(APIException.class)
    public APIResponse<Object> handleApiException(HttpServletRequest request, APIException ex) {
        log.error("process url {} failed", request.getRequestURL().toString(), ex);
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setCode(ex.getErrorCode());
        apiResponse.setMessage(ex.getErrorMessage());
        return apiResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public APIResponse<Object> handleException(NoHandlerFoundException ex) {
        log.error(ex.getMessage(), ex);
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setCode(4000);
        apiResponse.setMessage(ex.getMessage());
        return apiResponse;
    }


    @ExceptionHandler
    public APIResponse<Object> handle(HttpServletRequest req, HandlerMethod method, Exception ex) {
        if (ex instanceof BusinessException) {
            BusinessException exception = (BusinessException) ex;
            log.warn(String.format("访问 %s -> %s 出现业务异常！", req.getRequestURI(), method.toString()), ex);
            // exception.getCode()
            return new APIResponse<>(4000, false, null, exception.getMessage());
        } else {
            log.error(String.format("访问 %s -> %s 出现系统异常！", req.getRequestURI(), method.toString()), ex);
            return new APIResponse<>(GENERIC_SERVER_ERROR_CODE, false, null, GENERIC_SERVER_ERROR_MESSAGE);
        }
    }


    /**
     * 仅当方法或类没有标记@NoAPIResponse才自动包装
     *
     * @param returnType    返回类型
     * @param converterType 转换类型
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() != APIResponse.class
                && AnnotationUtils.findAnnotation(returnType.getMethod(), NoAPIResponse.class) == null
                && AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), NoAPIResponse.class) == null;
    }

    /**
     * 自动包装外层APIResposne响应
     * <p>
     * 如果控制器返回的是String类型，那么返回APIResponse 对象会出现转换错误，因为我们在控制器返回的是String类型
     * springmvc会使用 StringHttpMessageConverter 转换器，这时候会报转换错误。
     * 解决方式：把MappingJackson2HttpMessageConverter放在最前面即可，或者在ResponseBodyAdvice里面判断类型，特殊处理
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("OK");
        apiResponse.setCode(2000);
        apiResponse.setData(body);
        // 特殊处理返回值为String的类型
        if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(apiResponse);
        } else {
            return apiResponse;
        }
    }



}