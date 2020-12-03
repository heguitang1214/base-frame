//package com.tang.project.config;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.db.sql.StatementWrapper;
//import com.alibaba.druid.util.JdbcConstants;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.binding.MapperMethod;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.*;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ClassUtils;
//
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 数据权限查询SQL重写
// */
//@Intercepts({
//        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
//        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//})
//@Component
//@Slf4j
//public class DataPermissionSqlInterceptor1 implements Interceptor {
//
//    private final static String DB_TYPE = JdbcConstants.MYSQL;
//
//    private static final String SPACE = " ";
//    private static final String SELECT = "select";
//    private static final String DELETE = "delete";
//    private static final String UPDATE = "update";
//    private static final String INSERT = "insert";
//    private static final String WHERE = "WHERE";
//    private static final String FROM = "FROM";
//    private static final String AND = "AND";
//
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        Object[] args = invocation.getArgs();
//        if (target instanceof ParameterHandler) {
////            ParameterHandler parameterHandler = (ParameterHandler) target;
////            Object parameterObject = parameterHandler.getParameterObject();
////
////            System.out.println(parameterObject);
////
////
//////            parameterHandler.setParameters();
////
////
////            PreparedStatement ps = (PreparedStatement) args[0];
////            int parameterCount = ps.getParameterMetaData().getParameterCount();
////            ps.clearParameters();
////            ps.setString(1,"唐家三少");
////            ps.setInt(2, 11);
////            ps.setString(3, "男1");
//
//
//            // 反射获取 参数对像
////            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
////            parameterField.setAccessible(true);
////            Object parameterObject = parameterField.get(parameterHandler);
//
//            // 改写参数
////            parameterObject = processSingle(parameterObject, paramNames);
////            Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
////            boundSqlField.setAccessible(true);
////            BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);
////
////            List<String> paramNames = new ArrayList<>();
////            Map<String, Object> paramMap = new MapperMethod.ParamMap<>();
////            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
////
////            for (ParameterMapping parameterMapping : parameterMappings) {
////                paramNames.add(parameterMapping.getProperty());
////            }
////
////            if (parameterObject == null) {
////                paramMap.put("id", 1);
////                paramMap.put("sex", "男");
////                parameterObject = paramMap;
////                // 单参数 将 参数转为 map
////            } else if (ClassUtils.isPrimitiveOrWrapper(parameterObject.getClass())
////                    || String.class.isAssignableFrom(parameterObject.getClass())
////                    || Number.class.isAssignableFrom(parameterObject.getClass())) {
////                if (paramNames.size() == 1) {
////                    paramMap.put(paramNames.iterator().next(), parameterObject);
////                    paramMap.put("id", 1);
////                    paramMap.put("sex", "男");
////                    parameterObject = paramMap;
////                }
////            } else {
////                // 处理参数对象  如果是 map 且map的key 中没有 tenantId，添加到参数map中
////                // 如果参数是bean，反射设置值
////                if (parameterObject instanceof Map) {
////                    ((Map) parameterObject).putIfAbsent("id", 1);
////                    ((Map) parameterObject).putIfAbsent("sex", "男");
////                } else {
////                    throw new RuntimeException("xx");
////                }
////
////            }
////
////            // 改写的参数设置到原parameterHandler对象
////            parameterField.set(parameterHandler, parameterObject);
////            parameterHandler.setParameters(ps);
//        }
//
//        if (target instanceof StatementHandler) {
//            StatementHandler statementHandler = (StatementHandler) target;
//
//            statementHandler.getParameterHandler().setParameters();
//
//        }
//
//        if (target instanceof Executor) {
//            Object parameter = args[1];
//            boolean isUpdate = args.length == 2;
//            MappedStatement ms = (MappedStatement) args[0];
//
//            ParameterMap parameterMap = ms.getParameterMap();
//            List<ParameterMapping> list = parameterMap.getParameterMappings();
//
//
//            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
//                BoundSql boundSql;
//                if (args.length == 4) {
//                    boundSql = ms.getBoundSql(parameter);
//                } else {
//                    boundSql = (BoundSql) args[5];
//                }
//                // 判断当前的SQL是否需要进行数据权限的控制
//                String sql = boundSql.getSql();
//                boundSql.setAdditionalParameter("id", 1);
//                boolean ignore = ignoreDataPermission();
//                if (ignore) {
//                    return invocation.proceed();
//                }
//
//                log.info("修改前原始查询SQL语句为：{}", sql);
//                sql = sqlRewrite(sql, ms.getSqlCommandType());
//                log.info("改写后的权限查询SQL语句为：{}", sql);
//
//                // 重新new一个查询语句对像
//                BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),
//                        sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
//                // 把新的查询放到statement里
//                MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
//                for (ParameterMapping mapping : boundSql.getParameterMappings()) {
//                    String prop = mapping.getProperty();
//                    if (boundSql.hasAdditionalParameter(prop)) {
//                        newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
//                    }
//                }
//                args[0] = newMs;
//            }
//        }
//        return invocation.proceed();
//    }
//
//
//    private boolean ignoreDataPermission() {
//        return false;
//    }
//
//
//    private String sqlRewrite(String sql, SqlCommandType type) {
//        return sqlRewrite(sql, type, null, null);
//    }
//
//    private String sqlRewrite(String sql, SqlCommandType type, Object param, Object parameter) {
//        // select 条件
//        if (SqlCommandType.SELECT == type) {
//            return sqlRewriteSelect(sql);
//        }
//        return sql;
//    }
//
//
//    private String sqlRewriteSelect(String sql) {
//        StringBuilder sb = new StringBuilder();
//
////        String selectSql = "";
////        // 规则：管控字段 + 固定字段（SQL语句中原有的字段）
////        String addColomn = SELECT + SPACE + selectSql + ",";
////        sql = StringUtils.replaceIgnoreCase(sql, SELECT, addColomn);
//        sb.append(sql);
//
//        // where 条件的限制
//        List<String> rowRuleDtoList = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(rowRuleDtoList)) {
//            if (!StringUtils.containsIgnoreCase(sql, WHERE)) {
//                sb.append(SPACE).append(WHERE).append(SPACE);
//            }
//            for (String str : rowRuleDtoList) {
//                sb.append(SPACE).append(AND).append(SPACE).append(str);
//            }
//        }
//        return sb.toString();
//    }
//
//
//    /**
//     * 包装目标对象 包装：为目标对象创建一个代理对象
//     * 根据当前拦截的对象创建了一个动态代理对象。代理对象的InvocationHandler处理器为新建的Plugin对象
//     * 官方推荐实现方式：Plugin.wrap(target, this);
//     */
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
//        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
//        builder.resource(ms.getResource());
//        builder.fetchSize(ms.getFetchSize());
//        builder.statementType(ms.getStatementType());
//        builder.keyGenerator(ms.getKeyGenerator());
//        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
//            builder.keyProperty(ms.getKeyProperties()[0]);
//        }
//        builder.timeout(ms.getTimeout());
//        builder.parameterMap(ms.getParameterMap());
//        builder.resultMaps(ms.getResultMaps());
//        builder.resultSetType(ms.getResultSetType());
//        builder.cache(ms.getCache());
//        builder.flushCacheRequired(ms.isFlushCacheRequired());
//        builder.useCache(ms.isUseCache());
//
////        builder.keyColumn(StringUtils.join(ms.getKeyColumns(), ','));
////        builder.lang(ms.getLang()).parameterMap(ms.getParameterMap());
////        builder.resultOrdered(ms.isResultOrdered());
////        builder.resultSets(StringUtils.join(ms.getResultSets(), ','));
//        return builder.build();
//    }
//
//    public static class BoundSqlSqlSource implements SqlSource {
//
//        private BoundSql boundSql;
//
//        public BoundSqlSqlSource(BoundSql boundSql) {
//            this.boundSql = boundSql;
//        }
//
//        @Override
//        public BoundSql getBoundSql(Object parameterObject) {
//            return boundSql;
//        }
//    }
//}
//
//
///*
//方式一：使用 boundSql.setAdditionalParameter("id", 1); 没有效果
//方式二：使用 PreparedStatement设置参数下标，参数值
//方式三：使用 SQL解析组件
//
//
//
// */