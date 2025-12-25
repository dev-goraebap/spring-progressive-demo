package xyz.goraebap.blog.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Spring Progressive Demo API")
                .description("Spring Boot Progressive Web App Demo API Documentation")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Goraebap")
                    .url("https://goraebap.xyz")
                    .email("contact@goraebap.xyz"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT"))
            );
    }
}
