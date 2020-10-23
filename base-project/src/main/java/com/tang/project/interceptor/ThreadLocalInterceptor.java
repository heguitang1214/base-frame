package com.tang.project.interceptor;

import com.tang.project.dto.UserDto;
import com.tang.project.utils.ThreadLocalUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ThreadLocal 使用demo
 */
@Component
public class ThreadLocalInterceptor implements HandlerInterceptor {

    /**
     * ThreadLocal 使用demo
     *
     * @param request  请求
     * @param response 响应
     * @param handler  handler
     * @return 是否通过
     * @throws Exception 异常信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userAgent = request.getHeader("User-Agent");

        UserDto userDto = new UserDto();
        ThreadLocalUtils.setUser(userDto);

        return true;
    }

    /**
     * 使用完后，需要进行清除
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }
}
