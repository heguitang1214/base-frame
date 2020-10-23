//package com.tang.project.config;

//import cn.hutool.core.collection.CollectionUtil;
//import com.alibaba.druid.sql.SQLUtils;
//import com.alibaba.druid.sql.ast.SQLExpr;
//import com.alibaba.druid.sql.ast.SQLStatement;
//import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
//import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
//import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
//import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
//import com.alibaba.druid.util.JdbcConstants;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.executor.parameter.ParameterHandler;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.mapping.*;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.sql.PreparedStatement;
//import java.util.*;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
///**
// * 数据权限查询SQL重写
// */
//@Intercepts({
//        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
////        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//})
//@Component
//@Slf4j
//public class DataPermissionSqlInterceptor implements Interceptor {
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
//    static {
//        try {
//            InputStream inputStream = Resources.getResourceAsStream("dataPermission.json");
//            DataPermissionDTO dataPermissionDTO = JsonUtil.parse(inputStream, DataPermissionDTO.class);
//            ThreadLocalUtils.setDataPermission(dataPermissionDTO);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        Object[] args = invocation.getArgs();
//        if (target instanceof ParameterHandler) {
//            ParameterHandler parameterHandler = (ParameterHandler) target;
//            Object parameterObject = parameterHandler.getParameterObject();
//            PreparedStatement ps = (PreparedStatement) args[0];
//
//        }
//
//        if (target instanceof Executor) {
//            Object parameter = args[1];
//            boolean isUpdate = args.length == 2;
//            MappedStatement ms = (MappedStatement) args[0];
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
//            // update 操作
//            if (isUpdate) {
//                log.info("=================更新操作===============");
//                BoundSql boundSql = ms.getBoundSql(parameter);
//                String sql = boundSql.getSql();
//                log.info("修改前原始更新SQL语句为：{}", sql);
//                sql = sqlRewrite(sql, ms.getSqlCommandType(), parameter);
//                log.info("改写后的权限更新SQL语句为：{}", sql);
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
//        return sqlRewrite(sql, type, ThreadLocalUtils.getDataPermission(), null);
//    }
//
//    private String sqlRewrite(String sql, SqlCommandType type, Object parameter) {
//        return sqlRewrite(sql, type, ThreadLocalUtils.getDataPermission(), parameter);
//    }
//
//    private String sqlRewrite(String sql, SqlCommandType type, DataPermissionDTO param, Object parameter) {
//        permissionParamCheck(param);
//        // insert 条件
//        if (SqlCommandType.INSERT == type) {
//            return sqlRewriteInsert(sql, param, parameter);
//        }
//        // update 条件
//        if (SqlCommandType.UPDATE == type) {
//            return sqlRewriteUpdate(sql, param);
//        }
//        // select 条件
//        if (SqlCommandType.SELECT == type) {
//            return sqlRewriteSelect(sql, param);
//        }
//        return sql;
//    }
//
//    private String sqlRewriteInsert(String sql, DataPermissionDTO param, Object parameter) {
//        // SQL 解析
//        SQLStatement sqlStatements = SQLUtils.parseSingleMysqlStatement(sql);
//        List<DataFieldDTO> addColomnList = param.getFieldRuleDTO().getAddColomnList();
//        if (CollectionUtil.isEmpty(addColomnList)) {
//            throw new RuntimeException("添加权限中的权限数据字段为空！");
//        }
//        Map<String, List<DataFieldDTO>> addmap = addColomnList.stream().collect(Collectors.groupingBy(DataFieldDTO::getTableName));
//
//        // 删除没有权限的字段
//        SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatements;
//        String insertTableName = sqlInsertStatement.getTableName().getSimpleName();
//
//        if (!addmap.containsKey(insertTableName)) {
////            throw new RuntimeException("当前用户的数据权限，没有办法操作表：" + insertTableName);
//        }
////        List<String> insertCol = addmap.get(insertTableName).stream().map(DataFieldDTO::getFieldName).collect(Collectors.toList());
//
//        // SQL改写
//        Field[] declaredFields = parameter.getClass().getDeclaredFields();
//        for (Field field : declaredFields) {
//            // todo 使用insertCol 来进行判断
//            if ("name".equals(field.getName())) {
//                field.setAccessible(true);
//                try {
//                    // 数据库中某些字段可能不能为空，这里可以根据类型来判断
//                    field.set(parameter, null);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return SQLUtils.toSQLString(sqlInsertStatement, DB_TYPE);
//    }
//
//    private String sqlRewriteUpdate(String sql, DataPermissionDTO param) {
//        // SQL 解析
//        // 官网地址：https://github.com/alibaba/druid/wiki/SQL-Parser
////        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//        SQLStatement sqlStatements = SQLUtils.parseSingleMysqlStatement(sql);
//        List<DataFieldDTO> editColomnList = param.getFieldRuleDTO().getEditColomnList();
//        if (CollectionUtil.isEmpty(editColomnList)) {
//            throw new RuntimeException("修改权限中的权限数据字段为空！");
//        }
//        Map<String, List<DataFieldDTO>> editmap = editColomnList.stream().collect(Collectors.groupingBy(DataFieldDTO::getTableName));
//
//        // 删除没有权限的字段
//        SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatements;
//        String updatTableName = sqlUpdateStatement.getTableName().getSimpleName();
//
//        if (!editmap.containsKey(updatTableName)) {
////            throw new RuntimeException("当前用户的数据权限，没有办法操作表：" + updatTableName);
//        }
////        List<String> updateCol = editmap.get(updatTableName).stream().map(DataFieldDTO::getFieldName).collect(Collectors.toList());
//
//        // 对insert的字段进行判断，获取列名
////        sqlInsertStatement.accept(visitor);
//
//        List<SQLUpdateSetItem> items = sqlUpdateStatement.getItems();
//        ListIterator<SQLUpdateSetItem> it = items.listIterator();
//        while (it.hasNext()) {
//            SQLUpdateSetItem next = it.next();
//            SQLExpr sqlExpr = next.getColumn();
//            if (sqlExpr instanceof SQLIdentifierExpr) {
//                String columnName = ((SQLIdentifierExpr) sqlExpr).getName();
//                // todo 这里需要做条件判断
//                if ("name".equals(columnName)) {
//                    it.remove();
//                }
//            }
//        }
//        return SQLUtils.toSQLString(sqlUpdateStatement, DB_TYPE);
//    }
//
//    private String sqlRewriteSelect(String sql, DataPermissionDTO param) {
//        StringBuilder sb = new StringBuilder();
//
//        String selectSql = param.getFieldRuleDTO().getSelectSQL();
//        List<String> rowRuleDtoList = new ArrayList<>();
//        List<RowRuleDTO> rowRules = param.getRowRuleDTOS();
//        if (CollectionUtil.isEmpty(rowRules)) {
//            rowRuleDtoList = rowRules.stream().map(RowRuleDTO::getSqlSentence).collect(Collectors.toList());
//        }
////            String showColomn = SELECT + SPACE + param.getSelectSql() + SPACE;
////            int index = StringUtils.indexOfIgnoreCase(sql, FROM);
////            if (index < 0) {
////                throw new RuntimeException("权限改写前的SQL语句有误，SQL语句中不包含FORM关键字");
////            }
////            sb.replace(0, index, showColomn);
//        // 规则：管控字段 + 固定字段（SQL语句中原有的字段）
//        String addColomn = SELECT + SPACE + selectSql + ",";
//        sql = StringUtils.replaceIgnoreCase(sql, SELECT, addColomn);
//        sb.append(sql);
//
//        // where 条件的限制
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
//    private void permissionParamCheck(DataPermissionDTO param) {
//        if (param == null) {
//            throw new RuntimeException("当前用户对应的权限数据为空");
//        }
//        if (param.getFieldRuleDTO() == null) {
//            throw new RuntimeException("前用户对应的字段显示规则为空");
//        }
//    }
//
//
//    private void checkSql(String sql) {
//        // TODO: 2020/10/21 检查SQL
//        if (StringUtils.isEmpty(sql)) {
//            log.error("当前用户没有权限查询对应的数据");
//            throw new RuntimeException("当前用户没有权限查询对应的数据");
//
//        }
//        boolean valid = isValid(sql);
//        if (!valid) {
//            throw new RuntimeException("SQL校验不通过");
//        }
//    }
//
//    /**
//     * 正则表达式
//     **/
//    private static final String REG = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
//            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|drop|execute)\\b)";
//
//    //b  表示 限定单词边界  比如  select 不通过   1select则是可以的
//    private static Pattern sqlPattern = Pattern.compile(REG, Pattern.CASE_INSENSITIVE);
//
//    private boolean isValid(String str) {
//        if (sqlPattern.matcher(str).find()) {
//            log.error("未能通过过滤器：str=" + str);
//            return false;
//        }
//        return true;
//    }
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