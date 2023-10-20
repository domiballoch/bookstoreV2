package dom.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value(value = "${spring.application.name}")
    private String name;

    @Value(value = "${spring.application.description}")
    private String description;

    @Value(value = "${spring.build.version})")
    private String version;

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title(name)
                        .description(description)
                        .version(version)
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
