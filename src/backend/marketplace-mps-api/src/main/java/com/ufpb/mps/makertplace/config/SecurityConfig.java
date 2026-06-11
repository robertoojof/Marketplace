package com.ufpb.mps.makertplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] ROTAS_PUBLICAS = {
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/api/health",
      // "/api/produtos/catalogo"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) // Desabilita CSRF
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(ROTAS_PUBLICAS)
            .permitAll()
            // Define que qualquer outra rota precisa de autenticação
            .anyRequest().authenticated());

    return http.build();
  }
}