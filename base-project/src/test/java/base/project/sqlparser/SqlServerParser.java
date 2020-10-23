//package base.project.sqlparser;
//
//import com.alibaba.druid.sql.SQLUtils;
//import com.alibaba.druid.sql.ast.SQLExpr;
//import com.alibaba.druid.sql.ast.SQLStatement;
//import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
//import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
//import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
//import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
//import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
//import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
//import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
//import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
//import com.alibaba.druid.util.JdbcConstants;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author li.dongquan
// * @Description: SQLServer解析
// * @Date 2020/10/16
// */
//public class SqlServerParser {
//
//    /**
//     * 解析sql语句
//     *
//     * @param statement
//     * @return
//     */
//    public void parse(String statement) {
//        // 使用druid解析语句
//        // 第一个参数为SQL语句
//        // 第二个参数为解析的数据库类型
//        List<SQLStatement> statementList = SQLUtils.parseStatements(statement, JdbcConstants.SQL_SERVER);
//        // 单语句解析，只有一条数据
//        if (!statement.isEmpty()) {
//            SQLStatement sqlStatement = statementList.get(0);
//            // 插入语句解析
//            if (sqlStatement instanceof SQLServerInsertStatement) {
//                // 转换
//                SQLServerInsertStatement insertStatement = (SQLServerInsertStatement) sqlStatement;
//                // 获取列名
//                List<SQLExpr> columns = insertStatement.getColumns();
//                List<String> columnsName = new ArrayList<>(columns.size());
//                for (SQLExpr column : columns) {
//                    columnsName.add(((SQLIdentifierExpr) column).getName());
//                }
//                System.out.println(columnsName);
//                // 获取值
//                List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
//                List<List<Object>> dataList = new ArrayList<>();
//                for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
//                    List<SQLExpr> values = valuesClause.getValues();
//                    List<Object> data = new ArrayList<>(columnsName.size());
//                    for (SQLExpr value : values) {
//                        data.add(getValue(value));
//                    }
//                    dataList.add(data);
//                }
//                System.out.println(dataList);
//                // 获取表名
//                System.out.println(insertStatement.getTableName().getSimpleName());
//            } else if (sqlStatement instanceof SQLServerUpdateStatement) {
//                // 更新语句解析
//                SQLServerUpdateStatement updateStatement = (SQLServerUpdateStatement) sqlStatement;
//                // 获取更新的值和内容
//                List<SQLUpdateSetItem> items = updateStatement.getItems();
//                Map<String, Object> updateMap = new HashMap<>(items.size());
//                for (SQLUpdateSetItem item : items) {
//                    updateMap.put(((SQLIdentifierExpr) item.getColumn()).getName(), getValue(item.getValue()));
//                }
//                System.out.println(updateMap);
//                // 获取条件，条件比较复杂，需根据实际情况自行提取
//                SQLBinaryOpExpr where = (SQLBinaryOpExpr) updateStatement.getWhere();
//                System.out.println(where);
//                // 获取表名
//                System.out.println(updateStatement.getTableName().getSimpleName());
//            }
//        }
//    }
//
//    private static Object getValue(SQLExpr value) {
//        // TODO 判断更多的种类
//        if (value instanceof SQLIntegerExpr) {
//            // 值是数字
//            return ((SQLIntegerExpr) value).getNumber();
//        } else if (value instanceof SQLCharExpr) {
//            // 值是字符串
//            return ((SQLCharExpr) value).getText();
//        }
//        return null;
//    }
//
//    public static void main(String[] args) throws Exception {
//        SqlServerParser sqlServerParser = new SqlServerParser();
//        sqlServerParser.parse("update test set status='P' where id=20");
//		sqlServerParser.parse("insert into test (id,status,name,ce,acc) values (29,'P','lll','sxsx','Arferwg')");
//    }
//}
//
