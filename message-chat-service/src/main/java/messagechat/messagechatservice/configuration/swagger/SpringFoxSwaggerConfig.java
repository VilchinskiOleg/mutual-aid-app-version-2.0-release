package messagechat.messagechatservice.configuration.swagger;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Just plug this properties to context, for using in future:
 */
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

    /**
     * For fix problem with HATEOAS:
     */
    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
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