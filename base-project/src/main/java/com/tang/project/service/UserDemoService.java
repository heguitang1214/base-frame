package com.tang.project.service;

import com.tang.common.utils.ReflectionsUtils;
import com.tang.project.dto.DataPermissionBase;
import com.tang.project.dto.UserDemoDto;
import com.tang.project.entry.UserDemo;
import com.tang.project.utils.DataPermissionUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDemoService {


    public List<UserDemoDto> list() {
        List<UserDemoDto> result = new ArrayList<>();
        UserDemo userDemo = new UserDemo();
        userDemo.setId("4");
        userDemo.setUserName("张三");

        UserDemo userDemo1 = new UserDemo();
        userDemo1.setId("200");


        UserDemoDto userDemoDto = convertEntry(userDemo);
        UserDemoDto userDemoDto1 = convertEntry(userDemo1);

        result.add(userDemoDto);
        result.add(userDemoDto1);


        return (List<UserDemoDto>) dataPermissionPack(result);
//        return result;

    }

    public static List<? extends DataPermissionBase> dataPermissionPack(List<? extends DataPermissionBase> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return new ArrayList<>();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("SysOrgCode", "张三");

        for (DataPermissionBase data : dataList) {
            String str = "{{userName}}==#{SysOrgCode} && {{id}}==4";
            List<String> list = DataPermissionUtil.analyticExpression(str);

            // 环境变量赋值
            Map<String, Object> sysVariable = new HashMap<>();
            Map<String, Object> expVariable = new HashMap<>();
            Map<String, Map<String, Object>> variable = new HashMap<>();
            for (String expression : list) {
                Object value = map.get(expression);
                if (value != null) {
                    sysVariable.put(expression, value);
                } else {
                    value = ReflectionsUtils.getFieldValue(data, expression);
                    expVariable.put(expression, value);
                }
            }
            variable.put("sys", sysVariable);
            variable.put("exp", expVariable);
            boolean eval = DataPermissionUtil.eval(str, list, variable);
            if (eval) {
                data.setOperation(Arrays.asList("111", "222"));
            }
        }
        return dataList;
    }


    private UserDemoDto convertEntry(UserDemo userDemo) {
        UserDemoDto userDemoDto = new UserDemoDto();
        userDemoDto.setId(userDemo.getId());
        userDemoDto.setUserName(userDemo.getUserName());
        return userDemoDto;
    }


}
