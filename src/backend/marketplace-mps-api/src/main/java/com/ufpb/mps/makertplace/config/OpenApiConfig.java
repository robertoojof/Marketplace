package com.ufpb.mps.makertplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        .info(new Info()
            .title("Marketplace MPS API")
            .version("0.0.1")
            .description("""
                  Documentação da API para o projeto Marketplace (Disciplina MPS - UFPB) \n
                  Desenvolvedores: \n
                            - João Roberto de Oliveira Ferreira - Matrícula: 20200083646 \n
                            - Gabriel Dantas de Sousa - Matrícula: 20200154031
                """))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        // Espera JWT no header Authorization para autenticação
        .components(new Components()
            .addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
