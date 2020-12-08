package com.tang.project.config.mybatis330;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;

public class RightLeftParenthesis extends Parenthesis {

    public RightLeftParenthesis(Expression expression) {
        setExpression(expression);
    }

    @Override
    public String toString() {
        return super.getExpression() + ")";

    }
}
