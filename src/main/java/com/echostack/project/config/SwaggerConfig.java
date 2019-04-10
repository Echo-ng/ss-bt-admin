package com.echostack.project.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Echo
 * @Date: 2019/2/19 16:49
 * @Description:
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket testApi(@Value("${swagger.api.tile}") String title
                          ,@Value("${swagger.api.description}") String description
                          ,@Value("${swagger.api.version}") String version
                          ,@Value("${swagger.api.groupName}") String groupName) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .apiInfo(apiInfo);
    }
}
