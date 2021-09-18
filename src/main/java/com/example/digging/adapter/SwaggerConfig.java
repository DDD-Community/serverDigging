package com.example.digging.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        version = "User";
        title = "User API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.digging.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    @Bean
    public Docket apiV2() {
        version = "PostLink";
        title = "PostLink API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.digging.controller"))
                .paths(PathSelectors.ant("/postlink/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    @Bean
    public Docket apiV3() {
        version = "PostText";
        title = "PostText API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.digging.controller"))
                .paths(PathSelectors.ant("/posttext/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    @Bean
    public Docket apiV4() {
        version = "Recent";
        title = "Recent API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.digging.controller"))
                .paths(PathSelectors.ant("/recent"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    @Bean
    public Docket apiV5() {
        version = "Calendar";
        title = "Calendar API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.digging.controller"))
                .paths(PathSelectors.ant("/calendar/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "Swagger로 생성한 API Docs",
                version,
                "com.example.digging",
                new Contact("Contact Me", "www.example.com", "foo@example.com"),
                "Licenses",

                "www.example.com",

                new ArrayList<>());
    }
}