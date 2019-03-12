package com.ltc.mybatis.plus.controller;


import com.ltc.mybatis.plus.entity.User;
import com.ltc.mybatis.plus.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ltc
 * @since 2019-03-09
 */
@RestController
@RequestMapping("/plus/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public void demo(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

}
