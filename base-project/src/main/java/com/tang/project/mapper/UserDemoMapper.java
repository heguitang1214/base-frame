package com.tang.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tang.project.entry.UserDemo;

import java.util.List;


public interface UserDemoMapper extends BaseMapper<UserDemo> {

    List<UserDemo> sqlTest();

}
