package com.tang.project.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tang.project.entry.UserDemo;
import com.tang.project.mapper.UserDemoMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDemoService extends ServiceImpl<UserDemoMapper, UserDemo> {


    public void test() {
        LambdaQueryWrapper<UserDemo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDemo::getUserName, "唐家三少");
        queryWrapper.eq(UserDemo::getId, null);
        queryWrapper.eq(UserDemo::getSex, null);
        list(queryWrapper);

    }
}
