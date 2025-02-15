package br.com.todolist.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API")
                        .version("1.0.0")
                        .description("Documentação da API criada com Springdoc OpenAPI e Swagger UI")
                        .contact(new Contact()
                                .name("Anderson")
                                .url("https://github.com/Andersoncrs5")
                                .email("anderson.c.rms2005@gmail.com"))
                        .license(new License()
                                .name("Licença Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
