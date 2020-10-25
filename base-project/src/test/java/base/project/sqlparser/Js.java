package base.project.sqlparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Js {

    public final static String VARIABLE_NAME_PATTERN1 = "\\{\\{([A-Za-z_]+)}}";
    public final static String VARIABLE_NAME_PATTERN2 = "#\\{([A-Za-z_]+)}";

    public static void main(String[] args) {

        String str = "{{userName}}==#{SysOrgCode} && {{id}}==4";
        Pattern compile = Pattern.compile(VARIABLE_NAME_PATTERN1);
        Matcher matcher = compile.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
        }


        Pattern compile1 = Pattern.compile(VARIABLE_NAME_PATTERN2);
        Matcher matcher1 = compile1.matcher(str);
        while (matcher1.find()) {
            System.out.println(matcher1.group(0));
            System.out.println(matcher1.group(1));
        }

    }

}
