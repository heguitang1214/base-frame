package com.tang.project.config.mybatis330;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.ExpressionVisitor;

public class Test extends BinaryExpression {
    @Override
    public String getStringExpression() {
        return "";
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {

    }
}
