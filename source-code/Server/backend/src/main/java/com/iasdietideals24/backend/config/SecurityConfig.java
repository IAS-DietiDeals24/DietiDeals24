package com.iasdietideals24.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuriamo lo Spring Security
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {

        http
                .csrf(c -> c.disable()) // Disabilitiamo il csrf
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // Nel caso di eccezione restituiamo un codice 401
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/auth/**", "/**").permitAll()
                                .anyRequest().authenticated()
                ) // Configuriamo gli authorization routes
                .oauth2ResourceServer(c -> c.jwt(Customizer.withDefaults())); // Configuriamo l'OAuth 2.0 Resource Server

        return http.build();
    }
}
