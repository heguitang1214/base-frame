package com.tang.project.config.mybatis330;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

public class UnsignedExpression extends AndExpression {

    public UnsignedExpression(Expression leftExpression, Expression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public String getStringExpression() {
        return "";
    }
}