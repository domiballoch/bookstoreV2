package dom.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info().title("BookstoreV2")
                        .description("A personal project")
                        .version("0.0.1-snapshot")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
