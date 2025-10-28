package com.banking.account.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Accounts Microservices REST API documentation")
                        .version("V1")
                        .description("Rohan's Accounts Microservices REST API documentation")
                        .contact(new Contact()
                                .name("Rohan")
                                .email("rohan.rana.7351@gmail.com")
                                .url("https://accounts.rohan.rana.7351"))
                        .license(new License()
                                .name("Apache 2.0")));
    }
}
