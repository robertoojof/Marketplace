package com.ufpb.mps.makertplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Marketplace MPS API")
            .version("0.0.1")
            .description("""
                  Documentação da API para o projeto Marketplace (Disciplina MPS - UFPB) \n
                  Desenvolvedores: \n
                            - João Roberto de Oliveira Ferreira - Matrícula: 20200083646 \n
                            - Gabriel Dantas de Sousa - Matrícula: 20200154031
                """));
  }
}
