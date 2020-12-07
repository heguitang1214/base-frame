//package com.tang.project.config.mybatis330;
//
//import com.baomidou.mybatisplus.core.toolkit.*;
//import com.beust.jcommander.internal.Lists;
//import lombok.*;
//import net.sf.jsqlparser.expression.BinaryExpression;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Parenthesis;
//import net.sf.jsqlparser.expression.StringValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
//import net.sf.jsqlparser.expression.operators.relational.*;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
//import net.sf.jsqlparser.statement.select.*;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
//public class DataPermissionInnerInterceptor extends JsqlParserSupport {
//
//    /**
//     * 模拟查询条件的字段名
//     */
//    static List<String> attr = Lists.newArrayList("sex", "sex");
//
//    /**
//     * 模拟查询条件字段对应的值
//     */
//    static List<String> attrValue = Lists.newArrayList("100", "100");
//
//    /**
//     * 模拟操作的表名
//     */
//    List<String> tableName = Lists.newArrayList("user_demo");
//
//    /**
//     * 判断是做and拼接还是or拼接
//     */
//    private static boolean isOR = false;
//
//
//    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
//        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
//        mpBs.sql(parserSingle(mpBs.sql(), null));
//    }
//
//    @Override
//    protected void processSelect(Select select, int index, Object obj) {
//        processSelectBody(select.getSelectBody());
//        List<WithItem> withItemsList = select.getWithItemsList();
//        if (!CollectionUtils.isEmpty(withItemsList)) {
//            withItemsList.forEach(this::processSelectBody);
//        }
//    }
//
//    protected void processSelectBody(SelectBody selectBody) {
//        if (selectBody instanceof PlainSelect) {
//            processPlainSelect((PlainSelect) selectBody);
//        } else if (selectBody instanceof WithItem) {
//            WithItem withItem = (WithItem) selectBody;
//            if (withItem.getSelectBody() != null) {
//                processSelectBody(withItem.getSelectBody());
//            }
//        } else {
//            SetOperationList operationList = (SetOperationList) selectBody;
//            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
//                operationList.getSelects().forEach(this::processSelectBody);
//            }
//        }
//    }
//
//    /**
//     * 处理 PlainSelect
//     */
//    protected void processPlainSelect(PlainSelect plainSelect) {
//        FromItem fromItem = plainSelect.getFromItem();
//        if (fromItem instanceof Table) {
//            processSelectItems(plainSelect);
//
//            Table fromTable = (Table) fromItem;
//            // 不是忽略的表名词
//            if (tableName.contains(fromTable.getName())) {
//                for (int i = 0; i < 2; i++) {
//                    plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable, attr.get(i), attrValue.get(i), ">"));
//                    isOR = true;
//                }
//            }
//
//        } else {
//            processFromItem(fromItem);
//        }
//        List<Join> joins = plainSelect.getJoins();
//        if (joins != null && joins.size() > 0) {
//            joins.forEach(j -> {
////                processJoin(j);
//                processFromItem(j.getRightItem());
//                processJoinWhere(j, plainSelect);
//            });
//        }
//    }
//
//    /**
//     * 处理数据库查询列
//     *
//     * @param plainSelect 普通查询
//     */
//    private void processSelectItems(PlainSelect plainSelect) {
//        // 处理列
//        List<SelectItem> newSelect = new ArrayList<>();
//        for (SelectItem selectItem : plainSelect.getSelectItems()) {
//            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
//            System.out.println("==============列信息=============");
//            System.out.println("字段别名：" + selectExpressionItem.getAlias());
//            System.out.println("字段信息：" + selectExpressionItem.getExpression());
//
//            Expression expression = selectExpressionItem.getExpression();
//            if (expression instanceof Column) {
//                Column column = (Column) expression;
//                if (column.getColumnName().contains("name")) {
//                    newSelect.add(selectItem);
//                }
//            }
//            // 添加新列
////                Column column = new Column(fromTable,"id");
////                newSelect.add(new SelectExpressionItem(column));
//        }
//        plainSelect.setSelectItems(newSelect);
//    }
//
//
//    /**
//     * 处理子查询等
//     */
//    protected void processFromItem(FromItem fromItem) {
//        if (fromItem instanceof SubJoin) {
//            SubJoin subJoin = (SubJoin) fromItem;
//            if (subJoin.getJoinList() != null) {
//                subJoin.getJoinList().forEach(this::processJoin);
//            }
//            if (subJoin.getLeft() != null) {
//                processFromItem(subJoin.getLeft());
//            }
//        } else if (fromItem instanceof SubSelect) {
//            SubSelect subSelect = (SubSelect) fromItem;
//            if (subSelect.getSelectBody() != null) {
//                processSelectBody(subSelect.getSelectBody());
//            }
//        } else if (fromItem instanceof ValuesList) {
//            logger.debug("Perform a subquery, if you do not give us feedback");
//        } else if (fromItem instanceof LateralSubSelect) {
//            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
//            if (lateralSubSelect.getSubSelect() != null) {
//                SubSelect subSelect = lateralSubSelect.getSubSelect();
//                if (subSelect.getSelectBody() != null) {
//                    processSelectBody(subSelect.getSelectBody());
//                }
//            }
//        }
//    }
//
//    /**
//     * 处理联接语句
//     */
//    protected void processJoin(Join join) {
//        if (join.getRightItem() instanceof Table) {
//            Table fromTable = (Table) join.getRightItem();
//            if (!tableName.contains(fromTable.getName())) {
//                // 过滤退出执行
//                return;
//            }
//            // 做 on 的连接 todo 注意点
//            for (int i = 0; i < 2; i++) {
//                join.setOnExpression(builderExpression(join.getOnExpression(), fromTable, attr.get(i), attrValue.get(i), "="));
//            }
//        }
//    }
//
//
//    protected void processJoinWhere(Join join, PlainSelect plainSelect) {
//        if (join.getRightItem() instanceof Table) {
//            Table fromTable = (Table) join.getRightItem();
//            if (!tableName.contains(fromTable.getName())) {
//                // 过滤退出执行
//                return;
//            }
//
//            for (int i = 0; i < 2; i++) {
//                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable, attr.get(i), attrValue.get(i), "="));
//                isOR = true;
//            }
//        }
//    }
//
//    /**
//     * 处理条件
//     */
//    protected Expression builderExpression(Expression currentExpression, Table table, String name, String value) {
//        EqualsTo equalsTo = new EqualsTo();
//        equalsTo.setLeftExpression(this.getAliasColumn(table, name));
//        equalsTo.setRightExpression(new StringValue(value));
//        if (currentExpression == null) {
//            return equalsTo;
//        }
//        if (currentExpression instanceof BinaryExpression) {
//            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
//            doExpression(binaryExpression.getLeftExpression());
//            doExpression(binaryExpression.getRightExpression());
//        } else if (currentExpression instanceof InExpression) {
//            InExpression inExp = (InExpression) currentExpression;
//            ItemsList rightItems = inExp.getRightItemsList();
//            if (rightItems instanceof SubSelect) {
//                processSelectBody(((SubSelect) rightItems).getSelectBody());
//            }
//        }
//        if (currentExpression instanceof OrExpression) {
//            return new AndExpression(new Parenthesis(currentExpression), equalsTo);
//        } else {
//            return new AndExpression(currentExpression, equalsTo);
//        }
//    }
//
//    /**
//     * 处理条件
//     */
//    protected Expression builderExpression(Expression currentExpression, Table table, String attr, String attrValue, String rule) {
//        Expression expression = getExpression(table, attr, attrValue, rule);
//
//        if (currentExpression == null) {
//            return expression;
//        }
//        if (currentExpression instanceof BinaryExpression) {
//            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
//            doExpression(binaryExpression.getLeftExpression());
//            doExpression(binaryExpression.getRightExpression());
//        } else if (currentExpression instanceof InExpression) {
//            InExpression inExp = (InExpression) currentExpression;
//            ItemsList rightItems = inExp.getRightItemsList();
//            if (rightItems instanceof SubSelect) {
//                processSelectBody(((SubSelect) rightItems).getSelectBody());
//            }
//        }
//        if (currentExpression instanceof OrExpression) {
////            return new AndExpression(new Parenthesis(currentExpression), expression);
//            return new OrExpression(currentExpression, new RightLeftParenthesis(expression));
//        } else {
////            return new AndExpression(currentExpression, expression);
//            if (!isOR) {
//                return new AndExpression(currentExpression, new LeftParenthesis(expression));
//            } else {
//                return new OrExpression(currentExpression, new RightLeftParenthesis(expression));
//            }
//        }
//    }
//
//    protected void doExpression(Expression expression) {
//        if (expression instanceof FromItem) {
//            processFromItem((FromItem) expression);
//        } else if (expression instanceof InExpression) {
//            InExpression inExp = (InExpression) expression;
//            ItemsList rightItems = inExp.getRightItemsList();
//            if (rightItems instanceof SubSelect) {
//                processSelectBody(((SubSelect) rightItems).getSelectBody());
//            }
//        }
//    }
//
//    /**
//     * 字段别名设置
//     *
//     * @param table 表对象
//     * @return 字段
//     */
//    protected Column getAliasColumn(Table table, String name) {
//        StringBuilder column = new StringBuilder();
//        if (table.getAlias() != null) {
//            column.append(table.getAlias().getName()).append(StringPool.DOT);
//        }
//        column.append(name);
//        return new Column(column.toString());
//    }
//
//
//    private Expression getExpression(Table table, String attr, String attrValue, String rule) {
//
//        ComparisonOperator comparisonOperator = getComparisonOperator(table, attr, attrValue, rule);
//        if (comparisonOperator != null) {
//            return comparisonOperator;
//        }
//        if (rule.contains("in")) {
//            InExpression inExpression = new InExpression();
//            inExpression.setLeftExpression(this.getAliasColumn(table, attr));
//            List<Expression> expressions = new ArrayList<>();
//            for (String value : attrValue.split(",")) {
//                Expression expression = new StringValue(value);
//                expressions.add(expression);
//            }
//            inExpression.setRightItemsList(new ExpressionList(expressions));
//            if (rule.contains("not")) {
//                inExpression.setNot(true);
//            }
//            return inExpression;
//        } else {
//            throw new RuntimeException("数据权限行规则连接规则不支持：" + rule);
//        }
//    }
//
//    private ComparisonOperator getComparisonOperator(Table table, String attr, String attrValue, String rule) {
//        ComparisonOperator result = null;
//        if (Objects.equals(StringPool.EQUALS, rule)) {
//            result = new EqualsTo();
//        } else if (Objects.equals(StringPool.EXCLAMATION_MARK + StringPool.EQUALS, rule)) {
//            // 不等于
//            result = new NotEqualsTo();
//        } else if (Objects.equals(StringPool.RIGHT_CHEV + StringPool.EQUALS, rule)) {
//            // 大于等于
//            result = new GreaterThanEquals();
//        } else if (Objects.equals(StringPool.LEFT_CHEV + StringPool.EQUALS, rule)) {
//            // 小于等于
//            result = new MinorThanEquals();
//        } else if (Objects.equals(StringPool.RIGHT_CHEV, rule)) {
//            // 大于
//            result = new GreaterThan();
//        } else if (Objects.equals(StringPool.LEFT_CHEV, rule)) {
//            // 小于
//            result = new MinorThan();
//        } else {
//            return result;
//        }
//        result.setLeftExpression(this.getAliasColumn(table, attr));
//        result.setRightExpression(new StringValue(attrValue));
//        return result;
//    }
//
//}
//
//
