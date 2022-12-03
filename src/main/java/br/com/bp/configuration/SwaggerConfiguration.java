package br.com.bp.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Bate Papo Orientado a Eventos")
                        .version("1.0.0")
                        .description("CHAT APP BACKEND - SPRING BOOT, EVENT SOURCING AND CQRS"));
    }
}
