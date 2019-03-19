package com.ltc.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile({"dev", "test", "application.yml"})
@EnableSwagger2
@ConditionalOnBean(
        annotation = {EnableSwaggerConfiguration.class}
)
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
    @Autowired
    private SwaggerProperties swaggerProperties;

    public SwaggerAutoConfiguration() {
    }

    @Bean
    public Docket createRestApi() {
        LOGGER.info("创建swagger2Bean...");
        LOGGER.info("basePackage:{}", this.swaggerProperties.getBasePackage());
        LOGGER.info("title:{}", this.swaggerProperties.getTitle());
        LOGGER.info("createName:{}", this.swaggerProperties.getCreateName());
        LOGGER.info("url:{}", this.swaggerProperties.getUrl());
        LOGGER.info("email:{}", this.swaggerProperties.getEmail());
        LOGGER.info("version:{}", this.swaggerProperties.getVersion());
        LOGGER.info("description:{}", this.swaggerProperties.getDescription());
        return (new Docket(DocumentationType.SWAGGER_2))
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(this.swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder())
                .title(this.swaggerProperties.getTitle())
                .contact(new Contact(this.swaggerProperties.getCreateName(), this.swaggerProperties.getUrl(), this.swaggerProperties.getEmail()))
                .version(this.swaggerProperties.getVersion())
                .description(this.swaggerProperties.getDescription()).build();
    }
}
