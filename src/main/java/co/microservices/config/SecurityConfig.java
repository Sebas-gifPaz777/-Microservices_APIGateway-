package co.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                // Rutas públicas
                .authorizeExchange(ex -> ex
                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )
                // Login OIDC mediante Keycloak
                .oauth2Login(Customizer.withDefaults())
                // Validación de JWT en peticiones con token ya enviado
                .oauth2ResourceServer(rs -> rs.jwt())
                // Desactiva CSRF (solo APIs, sin formularios)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
}
