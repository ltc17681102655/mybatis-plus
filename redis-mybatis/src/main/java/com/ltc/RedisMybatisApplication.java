package com.ltc;

import com.ltc.swagger.EnableSwaggerConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(value = {"com.ltc.redis.mapper"})
@EnableSwaggerConfiguration
public class RedisMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisMybatisApplication.class, args);
    }

}
