package com.adc.da.swagger.config;

import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger.web.UiConfiguration.Constants;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@Profile({"prdnew", "devMysql", "devSqlServer"})
public class SwaggerConfig {
    @Value("${restPath:/api}")
    private String restPath;

    @Value("${swaggerLevel:1}")
    private int swaggerLevel;

    public SwaggerConfig() {
    }

    @Bean
    public Docket docket() {
        return this.initDocket();
    }

    private ApiInfo apiInfo() {
        return (new ApiInfoBuilder()).title("ADC-DA RESTful APIs").version("2.0.1").build();
    }

    private Docket initDocket() {
        Predicate apiPaths;
        switch (this.swaggerLevel) {
            case 0:
                apiPaths = PathSelectors.any();
                break;
            case 1:
                apiPaths = PathSelectors.regex(".*(" + this.restPath + "/).*");
                break;
            default:
                apiPaths = PathSelectors.none();
        }

        return (new Docket(DocumentationType.SWAGGER_2)).apiInfo(this.apiInfo()).select()
                                                        .apis(RequestHandlerSelectors.any()).paths(apiPaths).build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().deepLinking(true).displayOperationId(false).defaultModelsExpandDepth(1)
                                     .defaultModelExpandDepth(3).defaultModelRendering(ModelRendering.EXAMPLE)
                                     .displayRequestDuration(false).docExpansion(DocExpansion.NONE).filter(false)
                                     .maxDisplayedTags((Integer) null).operationsSorter(OperationsSorter.ALPHA)
                                     .showExtensions(false).tagsSorter(TagsSorter.ALPHA)
                                     .supportedSubmitMethods(Constants.DEFAULT_SUBMIT_METHODS)
                                     .validatorUrl((String) null).build();
    }
}
