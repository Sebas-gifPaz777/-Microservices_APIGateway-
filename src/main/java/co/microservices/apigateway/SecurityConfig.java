package co.microservices.apigateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Habilita la seguridad WebFlux
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Permite todas las solicitudes temporalmente
                )
                // --- Configuración de CSRF usando lambda (Spring Security 6.1+) ---
                .csrf(csrf -> csrf.disable()) // <-- Nueva forma de deshabilitar CSRF
                // --- Configuración de CORS usando lambda (Spring Security 6.1+) ---
                .cors(cors -> cors.disable()); // <-- Nueva forma de deshabilitar CORS

        return http.build();
    }
}