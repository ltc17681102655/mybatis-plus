package com.ltc;

import com.ltc.mybatis.plus.entity.TestEnum;
import com.ltc.mybatis.plus.mapper.TestEnumMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class MybatisPlusApplicationTests {

    @Autowired
    private TestEnumMapper testEnumMapper;

    @Test
    public void contextLoads() {
        //无值为空
        List<TestEnum> testEnums = testEnumMapper.selectList(null);
        System.out.println(testEnums);
        System.out.println(testEnums.isEmpty());
    }

    @Test
    public void demo(){
        List<TestEnum> testEnums = testEnumMapper.selectList(null);
        System.out.println(testEnums);
    }

}
