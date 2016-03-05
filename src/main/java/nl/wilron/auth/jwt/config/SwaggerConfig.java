package nl.wilron.auth.jwt.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger configuration.
 *
 * @author Ronald Moetwil
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(this.apiInfo())
                .groupName("Auth JWT - REST API")
                .select()
                .apis(RequestHandlerSelectors.basePackage("nl.wilron.auth.jwt.controller"))
                .paths(paths())
                .build()
                ;
    }

    private Predicate<String> paths() {
        return  regex("/*.*");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Auth JWT - REST API")
                .description("")
                .version("0.1.0")
                .build();
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfiguration.DEFAULT;
    }
}