package com.tang.project.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限的操作权限动态赋值
 */
@Aspect
@Component
public class DataPermissionOperation {

    // com.tang.project.service.UserDemoService

    @Pointcut("execution(public * com.tang.project.service..*.list*(..))")
    public void listMethodAfterAspect() {

    }

    @Pointcut("execution(public * com.tang.project.service..*.find*(..))")
    public void findMethodAfterAspect() {

    }

    @Pointcut("execution(public * com.tang.project.service..*.query*(..))")
    public void queryMethodAfterAspect() {

    }

    @Pointcut("execution(public * com.tang.project.service..*.select*(..))")
    public void selectMethodAfterAspect() {

    }

    @Pointcut("listMethodAfterAspect() || findMethodAfterAspect() || " +
            "queryMethodAfterAspect() || selectMethodAfterAspect()")
    public void methodAfterAspect() {

    }

    @After("methodAfterAspect()")
    public void aroundMethod(JoinPoint pjp) throws Throwable {
        System.out.println("method around start......");
        System.out.println("method around end......");
    }

    @AfterReturning(value = "methodAfterAspect()", returning = "obj")
    public void aroundMethod(JoinPoint pjp, Object obj) throws Throwable {

        System.out.println("第一个后置返回通知的返回值：" + obj);
        String typeName = obj.getClass().getTypeName();
        if (obj instanceof List) {

            for (Object o : (List) obj) {
                if (o instanceof DataPermissionOperation) {
                    System.out.println("xxxxxxxxxxxxxxxx");
                }
            }
        }

        System.out.println("修改完毕-->返回方法为:" + obj);

    }


    // 方法1：手动调用
    // 方法2：AOP切面，aService.list 调用 Bserver.list方法，aService.list 返回结果
    // 方法3：字节码注入

}
