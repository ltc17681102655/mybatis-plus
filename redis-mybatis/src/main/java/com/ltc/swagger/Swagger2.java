//package com.ltc.swagger;
//
//import com.google.common.base.Predicates;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * @Auther: ltc
// * @Date: 2019/3/13 11:46
// * @Description: http://localhost:8081/swagger-ui.html
// */
////让Spring来加载该类配置
//@Configuration
////开启Swagger2
//@EnableSwagger2
//public class Swagger2 {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("redis-mybatis")
//                .apiInfo(apiInfo())
//                .select()
//                // 扫描的包所在位置
//                .apis(RequestHandlerSelectors.basePackage("com.ltc"))
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
//                // 扫描的 URL 规则
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//
//    private ApiInfo apiInfo() {
//        // 联系信息
//        return new ApiInfoBuilder()
//                // 大标题
//                .title("swager-redis")
//                // 描述
//                .description("ltc")
//                // 服务条款 URL
//                .termsOfServiceUrl("www.memory3334.com")
//                // 版本
//                .version("1.0.0")
//                .build();
//    }
//}
