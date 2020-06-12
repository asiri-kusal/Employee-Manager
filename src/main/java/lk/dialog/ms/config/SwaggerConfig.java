/**
 * The SwaggerConfig class used for generate swagger documentation.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

        @Bean
        public Docket swaggerSpringfoxDocket() {
            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .useDefaultResponseMessages(false);

            docket = docket.select()
                           .paths(regex(DEFAULT_INCLUDE_PATTERN))
                           .build();
            return docket;
        }


        private ApiKey apiKey() {
            return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder()
                                  .securityReferences(defaultAuth())
                                  .forPaths(regex(DEFAULT_INCLUDE_PATTERN))
                                  .build();
        }

        private List<SecurityReference> defaultAuth() {
            AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
            authorizationScopes[0] = authorizationScope;
            return Lists.newArrayList(
                new SecurityReference("JWT", authorizationScopes));
        }

        private ApiInfo metaData() {
            return new ApiInfoBuilder()
                .title("Spring Boot REST API")
                .description("\"Spring Boot REST API\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Dialog", "https://www.dialog.lk/", "service@dialog.lk"))
                .build();
        }

}