//package base.project.sqlparser;
//
//import com.alibaba.druid.sql.SQLUtils;
//import com.alibaba.druid.sql.ast.SQLStatement;
//import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
//import com.alibaba.druid.util.JdbcConstants;
//
//import java.util.List;
//
//
///**
// * Hello world!
// *
// */
//public class App {
//
//    public static void main(String[] args) {
//
//        // String sql = "update t set name = 'x' where id < 100 limit 10";
////         String sql = "SELECT ID, NAME, AGE FROM USER WHERE ID = ? limit 2";
//         String sql = "SELECT t.id AS '租户套餐ID', t.system_id AS '系统ID',p.combo_name AS '套餐名称',\n" +
//                 "m.menu_id AS '系统服务菜单id',me.id AS '菜单ID',me.`name` AS '菜单名称'\n" +
//                 "FROM \n" +
//                 "tenant_product_combo t LEFT JOIN product_combo p ON (t.product_combo_id = p.id)\n" +
//                 "LEFT JOIN product_combo_server s ON (p.id = s.product_combo_id)\n" +
//                 "LEFT JOIN system_server_menu m ON (m.system_server_id = s.product_server_id)\n" +
//                 "LEFT JOIN poros_sec_menu me ON (m.menu_id = me.id)\n" +
//                 "WHERE t.tenant_id = 'W00001' AND t.system_id = '190cd14f5e825d452aa6a37d05e3ffdb'";
////         String sql = "select * from tablename limit 10";
//
////        String sql = "select user from emp_table";
//        String dbType = JdbcConstants.MYSQL;
//
//        //格式化输出
//        String result = SQLUtils.format(sql, dbType);
//        System.out.println(result); // 缺省大写格式
//        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
//
//        //解析出的独立语句的个数
//        System.out.println("size is:" + stmtList.size());
//        for (int i = 0; i < stmtList.size(); i++) {
//
//            SQLStatement stmt = stmtList.get(i);
//            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//            stmt.accept(visitor);
//
//            //获取表名称
//            System.out.println("Tables : " + visitor.getTables());
//            //获取操作方法名称,依赖于表名称
//            System.out.println("Manipulation : " + visitor.getTables());
//            //获取字段名称
//            System.out.println("fields : " + visitor.getColumns());
//        }
//
//    }
//
//}