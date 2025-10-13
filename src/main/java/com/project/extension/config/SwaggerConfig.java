package com.project.extension.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão da Empresa Léo Vidros")
                        .version("1.0.0")
                        .description("Esta API permite agendar, gerenciar serviços, orçamentos e estoque de produtos.")
                        .contact(new Contact()
                                .name("Equipe Léo Vidros")
                                .email("contato@leovidros.com.br")
                                .url("https://www.leovidros.com.br"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }

}
