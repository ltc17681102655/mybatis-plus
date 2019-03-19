package com.ltc.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SwaggerAutoConfiguration.class})
public @interface EnableSwaggerConfiguration {
}
