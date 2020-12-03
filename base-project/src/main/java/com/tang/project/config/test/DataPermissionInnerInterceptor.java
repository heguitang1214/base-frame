/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tang.project.config.test;

import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.PropertyMapper;
import com.beust.jcommander.internal.Lists;
import lombok.*;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * @author hubin
 * @since 3.4.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"rawtypes"})
public class DataPermissionInnerInterceptor extends JsqlParserSupport implements InnerInterceptor {

    private TenantLineHandler tenantLineHandler;

    static List<String> name = Lists.newArrayList("mobile", "password");
    static List<String> value = Lists.newArrayList("110", "123456");
    List<String> tableName = Lists.newArrayList("user_demo", "role_demo");

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (InterceptorIgnoreHelper.willIgnoreTenantLine(ms.getId())) return;
        if (SqlParserHelper.getSqlParserInfo(ms)) return;
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), null));
    }

    @Override
    protected void processSelect(Select select, int index, Object obj) {
        processSelectBody(select.getSelectBody());
        List<WithItem> withItemsList = select.getWithItemsList();
        if (!CollectionUtils.isEmpty(withItemsList)) {
            withItemsList.forEach(this::processSelectBody);
        }
    }

    protected void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                operationList.getSelects().forEach(this::processSelectBody);
            }
        }
    }

    /**
     * 处理 PlainSelect
     */
    protected void processPlainSelect(PlainSelect plainSelect) {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            // 不是忽略的表名词
//            if (!tenantLineHandler.ignoreTable(fromTable.getName())) {
            if (tableName.contains(fromTable.getName())) {
                //#1186 github
                // todo
//                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
//                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));

                for (int i = 0; i < 2; i++) {
                    plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable, name.get(i), value.get(i)));
                }
            }
        } else {
            processFromItem(fromItem);
        }
        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0) {
            joins.forEach(j -> {
//                processJoin(j);
                processFromItem(j.getRightItem());
                // TODO: 2020/12/3 添加where条件
                processJoinWhere(j, plainSelect);
            });
        }
    }

    /**
     * 处理子查询等
     */
    protected void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                subJoin.getJoinList().forEach(this::processJoin);
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            logger.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    /**
     * 处理联接语句
     */
    protected void processJoin(Join join) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
//            if (tenantLineHandler.ignoreTable(fromTable.getName())) {
            if (!tableName.contains(fromTable.getName())) {
                // 过滤退出执行
                return;
            }
//            todo
//            join.setOnExpression(builderExpression(join.getOnExpression(), fromTable));

            // 做 on 的连接 todo 注意点
//            for (int i = 0; i < 2; i++) {
//                join.setOnExpression(builderExpression(join.getOnExpression(), fromTable, name.get(i), value.get(i)));
//            }

        }
    }


    protected void processJoinWhere(Join join, PlainSelect plainSelect) {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
//            if (tenantLineHandler.ignoreTable(fromTable.getName())) {
            if (!tableName.contains(fromTable.getName())) {
                // 过滤退出执行
                return;
            }

            for (int i = 0; i < 2; i++) {
                plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable, name.get(i), value.get(i)));
            }
        }
    }

    /**
     * 处理条件
     */
    protected Expression builderExpression(Expression currentExpression, Table table, String name, String value) {
        EqualsTo equalsTo = new EqualsTo();
        // todo 这里做配置
//        equalsTo.setLeftExpression(this.getAliasColumn(table));
//        equalsTo.setRightExpression(tenantLineHandler.getTenantId());

        equalsTo.setLeftExpression(this.getAliasColumn(table, name));
        equalsTo.setRightExpression(new StringValue(value));
        if (currentExpression == null) {
            return equalsTo;
        }
        if (currentExpression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) currentExpression;
            doExpression(binaryExpression.getLeftExpression());
            doExpression(binaryExpression.getRightExpression());
        } else if (currentExpression instanceof InExpression) {
            InExpression inExp = (InExpression) currentExpression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
        if (currentExpression instanceof OrExpression) {
            return new AndExpression(new Parenthesis(currentExpression), equalsTo);
        } else {
            return new AndExpression(currentExpression, equalsTo);
        }
    }

    protected void doExpression(Expression expression) {
        if (expression instanceof FromItem) {
            processFromItem((FromItem) expression);
        } else if (expression instanceof InExpression) {
            InExpression inExp = (InExpression) expression;
            ItemsList rightItems = inExp.getRightItemsList();
            if (rightItems instanceof SubSelect) {
                processSelectBody(((SubSelect) rightItems).getSelectBody());
            }
        }
    }

    /**
     * 租户字段别名设置
     * <p>tenantId 或 tableAlias.tenantId</p>
     *
     * @param table 表对象
     * @return 字段
     */
    protected Column getAliasColumn(Table table, String name) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
//        column.append(tenantLineHandler.getTenantIdColumn());
        column.append(name);
        return new Column(column.toString());
    }

    protected Column getAliasColumnIn(Table table, String name) {
        StringBuilder column = new StringBuilder();
        if (table.getAlias() != null) {
            column.append(table.getAlias().getName()).append(StringPool.DOT);
        }
//        column.append(tenantLineHandler.getTenantIdColumn());
        column.append(name);
        return new Column(column.toString());
    }

    @Override
    public void setProperties(Properties properties) {
        PropertyMapper.newInstance(properties)
                .whenNotBlack("tenantLineHandler", ClassUtils::newInstance, this::setTenantLineHandler);
    }
}


