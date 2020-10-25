package com.tang.project.utils;

import com.tang.project.dto.DataPermissionBase;

import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataPermissionUtil {

    public final static String VARIABLE_NAME_PATTERN = "[A-Za-z_]+";

    public final static String SYS_VARIABLE_PATTERN = "[A-Za-z_]+";


    public static boolean eval(String expression, List<String> varNameList, Map<String, Map<String, Object>> variable) {
        Map<String, Object> sysVariable = variable.get("sys");
        Map<String, Object> expVariable = variable.get("exp");

        // 环境变量赋值
        for (String varName : varNameList) {
            Object value = sysVariable.get(varName);
            if (value != null) {
                if (value instanceof String) {
                    value = "'" + value + "'";
                }
                expression = expression.replace("#{" + varName + "}", value.toString());
            } else {
                value = expVariable.get(varName);
                if (value instanceof String) {
                    value = "'" + value + "'";
                }
                if (value == null) {
                    expression = expression.replace("{{" + varName + "}}", "");
                } else {
                    expression = expression.replace("{{" + varName + "}}", value.toString());
                }
            }
            System.out.println(expression);
        }
        try {
            Boolean testExecute7 = (Boolean) JavaScriptUtil.execute(expression);
            if (testExecute7 != null && testExecute7) {
                return true;
            }
        } catch (ScriptException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    // 复杂表达式怎么解析？？？

    /**
     * 解析表达式，获取表达式中的变量名称
     *
     * @param expression 异常信息
     * @return 变量名称
     */
    public static List<String> analyticExpression(String expression) {
        List<String> result = new ArrayList<>();
        Pattern compile = Pattern.compile(VARIABLE_NAME_PATTERN);
        Matcher matcher = compile.matcher(expression);
        while (matcher.find()) {
            String group = matcher.group();

            if (group.contains("_")) {
                // TODO: 2020/10/25 特殊兼容处理
                //group.toUpperCase()
            }
            result.add(group);
        }
        return result;
    }


    public static class Expression {
        private String leftVariable;
        private String rightVariable;
        private boolean isValue;

        public String getLeftVariable() {
            return leftVariable;
        }

        public void setLeftVariable(String leftVariable) {
            this.leftVariable = leftVariable;
        }

        public String getRightVariable() {
            return rightVariable;
        }

        public void setRightVariable(String rightVariable) {
            this.rightVariable = rightVariable;
        }

        public boolean isValue() {
            return isValue;
        }

        public void setValue(boolean value) {
            isValue = value;
        }
    }

}
