package co.microservices.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                // permisos públicos para el flujo de login + actuator si quieres
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login**", "/oauth2/**", "/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )
                // habilita el login mediante Keycloak (authorization_code)
                .oauth2Login(withDefaults())
                // habilita el resource server para validar JWT en cada petición
                .oauth2ResourceServer(rs -> rs.jwt(withDefaults()))

                // mantén deshabilitado CSRF/CORS si lo necesitas
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable());

        return http.build();
    }
}
