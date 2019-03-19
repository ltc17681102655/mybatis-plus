package com.ltc.swagger;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "swagger"
)
public class SwaggerProperties {
    private String basePackage = "com.ltc";
    private String title = "默认标题";
    private String createName = "默认创建者";
    private String url = "https://github.com/swagger-api/swagger-core/wiki/annotations";
    private String email = "默认邮箱";
    private String version = "1.0";
    private String description = "API 描述";

    public SwaggerProperties() {
    }

    public String getBasePackage() {
        return this.basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateName() {
        return this.createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}