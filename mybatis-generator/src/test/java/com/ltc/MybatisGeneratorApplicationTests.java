package com.ltc;

import com.alibaba.fastjson.JSONObject;
import com.ltc.mybatis.plus.entity.User;
import com.ltc.mybatis.plus.mapper.UserMapper;
import com.ltc.utils.JsonFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisGeneratorApplication.class)
public class MybatisGeneratorApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JsonFormat jsonFormat;

    @Test
    public void contextLoads() {
        List<User> users = userMapper.selectList(null);
//        System.out.println(users);
//        System.out.println(users.toString());
//        System.out.println(JsonFormat.jsonFormat(users.toString()));
        System.out.println(jsonFormat.Array(users));
//        System.out.println(jsonFormat.object(users.get(0).toString()));
//        System.out.println(users.get(0).toString());
//        System.out.println(JSONObject.toJSONString(users.get(0)));
    }

}
