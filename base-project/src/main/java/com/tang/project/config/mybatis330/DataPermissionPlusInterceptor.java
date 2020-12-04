//package com.tang.project.config.mybatis330;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.stereotype.Component;
//
///**
// * @author miemie
// * @since 3.4.0
// */
//@Intercepts({
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//})
//@Component
//@Slf4j
//public class DataPermissionPlusInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        Object[] args = invocation.getArgs();
//        if (target instanceof Executor) {
//            final Executor executor = (Executor) target;
//            Object parameter = args[1];
//            boolean isUpdate = args.length == 2;
//            MappedStatement ms = (MappedStatement) args[0];
//            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
//                RowBounds rowBounds = (RowBounds) args[2];
//                ResultHandler resultHandler = (ResultHandler) args[3];
//                BoundSql boundSql;
//                if (args.length == 4) {
//                    boundSql = ms.getBoundSql(parameter);
//                } else {
//                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
//                    boundSql = (BoundSql) args[5];
//                }
//
//                DataPermissionInnerInterceptor query = new DataPermissionInnerInterceptor();
//                // 处理SQL语句
//                query.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
//
//                CacheKey cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
//                return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
//            }
//        }
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof Executor || target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
//
//}
