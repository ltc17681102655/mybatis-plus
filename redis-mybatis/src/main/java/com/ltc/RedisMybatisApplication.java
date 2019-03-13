package com.ltc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.ltc.redis.mapper"})
public class RedisMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisMybatisApplication.class, args);
    }

}
