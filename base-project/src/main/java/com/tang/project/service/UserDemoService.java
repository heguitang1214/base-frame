package com.tang.project.service;

import com.tang.project.dto.UserDemoDto;
import com.tang.project.entry.UserDemo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDemoService {


    public List<UserDemoDto> list() {
        List<UserDemoDto> result = new ArrayList<>();
        UserDemo userDemo = new UserDemo();
        userDemo.setId("100");
        UserDemo userDemo1 = new UserDemo();
        userDemo1.setId("200");


        UserDemoDto userDemoDto = convertEntry(userDemo);
        UserDemoDto userDemoDto1 = convertEntry(userDemo1);

        result.add(userDemoDto);
        result.add(userDemoDto1);


        for (UserDemoDto userDemoDto2 : result) {

//            DataPermissionUtil
        }


        return result;

    }

    private UserDemoDto convertEntry(UserDemo userDemo) {
        UserDemoDto userDemoDto = new UserDemoDto();
        userDemoDto.setId(userDemo.getId());
        return userDemoDto;
    }


}
