//package base.project.sqlparser;
//
//import org.springframework.context.expression.MapAccessor;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.common.TemplateParserContext;
//import org.springframework.expression.spel.standard.SpelExpression;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class EL {
//
//    public static void main(String[] args) {
//        String skipExpress = "${value1==value2}";
//        Map map = new HashMap<>();
//        map.put("value1", 1);
//        map.put("value2", 1);
//        Boolean b = ExpressionParsingUtil.expressionParsing(skipExpress, map);
//        System.out.println(b);
//    }
//
//    public static Boolean expressionParsing(String skipExpress, Map map) {
//        if (StringUtils.isBlank(skipExpress) && map.isEmpty()) {
//            return false;
//        }
//        ExpressionParser parser = new SpelExpressionParser();
//        StandardEvaluationContext context = new StandardEvaluationContext();
//
//        TemplateParserContext templateParserContext = new TemplateParserContext("${", "}");
//        MapAccessor propertyAccessor = new MapAccessor();
//        context.setVariables(map);
//        context.setPropertyAccessors(Arrays.asList(propertyAccessor));
//
//        SpelExpression expression = (SpelExpression) parser.parseExpression(skipExpress, templateParserContext);
//        expression.setEvaluationContext(context);
//        boolean value = expression.getValue(map, boolean.class);
//        return value;
//    }
//
//}
