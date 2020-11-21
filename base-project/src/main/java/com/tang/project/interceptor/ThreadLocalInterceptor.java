package com.tang.project.interceptor;

import com.tang.project.dto.UserDto;
import com.tang.project.utils.ThreadLocalUtils;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * ThreadLocal 使用demo
 */
@Component
public class ThreadLocalInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalInterceptor.class);

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

//        InputStream inputStream = Resources.getResourceAsStream("test.json");
        // 读取本地文件中的数据
//        StringBuilder sb = new StringBuilder();
//        byte[] buffer = new byte[1024];
//        while (inputStream.read(buffer) != -1) {
//            sb.append(new String(buffer));
//        }
        
//        DataPermissionDTO dataPermissionDTO = JsonUtil.parse(inputStream, DataPermissionDTO.class);

        logger.info("ThreadLocalInterceptor 拦截器的 preHandle() 方法......");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("ThreadLocalInterceptor 拦截器的 postHandle() 方法......");
    }

    /**
     * 使用完后，需要进行清除
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

        logger.info("ThreadLocalInterceptor 拦截器的 afterCompletion() 方法......");
        ThreadLocalUtils.remove();
    }
}
