package com.iasdietideals24.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String CLAIM_COGNITO_GROUPS = "cognito:groups";

    // Configuriamo lo Spring Security
    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {

        http
                .csrf(c -> c.disable()) // Disabilitiamo il csrf
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // Nel caso di eccezione restituiamo un codice 401
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(new AntPathRequestMatcher("/aste/**", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/auth/**", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/categorie-asta/**", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/accounts/**", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/offerte/**/**/least-value", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/offerte/**/**/most-value", HttpMethod.GET.toString())).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/profili/**", HttpMethod.GET.toString())).permitAll()
                                .anyRequest().authenticated()
                ) // Configuriamo gli authorization routes
                .oauth2ResourceServer(this::oAuthRoleConversion); // Configuriamo l'OAuth 2.0 Resource Server

        return http.build();
    }

    // Mappiamo ogni cognito:group come un ruolo nella nostra app, in modo da settare le autorizzazioni
    private void oAuthRoleConversion(OAuth2ResourceServerConfigurer<HttpSecurity> oauth2) {
        oauth2.jwt(this::jwtToGrantedAuthExtractor);
    }

    private void jwtToGrantedAuthExtractor(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
        jwtConfigurer.jwtAuthenticationConverter(grantedAuthoritiesExtractor());
    }

    private Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::userAuthoritiesMapper);
        return converter;
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> userAuthoritiesMapper(Jwt jwt) {
        return mapCognitoAuthorities((List<String>) jwt.getClaims().getOrDefault(CLAIM_COGNITO_GROUPS, Collections.<String>emptyList()));
    }

    private List<GrantedAuthority> mapCognitoAuthorities(List<String> groups) {

        log.debug("Found cognito groups {}", groups);

        List<GrantedAuthority> mapped = new ArrayList<>();
        mapped.add(new SimpleGrantedAuthority(ROLE_USER));
        groups.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).forEach(mapped::add);

        log.debug("Roles: {}", mapped);

        return mapped;
    }
}
