package order.orderservice.configuration.swagger;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Collections;

@PropertySource(value = "classpath:swagger-api.properties")
@Configuration
@EnableSwagger2
public class SpringFoxSwaggerConfig {

    @Bean
    public Docket apiDocket(ApiProperties apiProperties) {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(createApiInfo(apiProperties));
    }

    private ApiInfo createApiInfo(ApiProperties apiProperties) {
        return new ApiInfo(
                apiProperties.getTitle(),
                apiProperties.getDescription(),
                apiProperties.getVersion(),
                null,
                createContact(apiProperties.getContact()),
                null,
                null,
                Collections.emptyList()
        );
    }

    private Contact createContact(ApiProperties.Contact contact) {
        return new Contact(
                contact.getName(),
                contact.getUrl(),
                contact.getEmail()
        );
    }
}
