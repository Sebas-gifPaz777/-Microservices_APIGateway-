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
                .anyExchange().permitAll() // <--- ¡Permite todas las solicitudes temporalmente!
            )
            .csrf().disable() // Deshabilita CSRF (simple para prueba, configurar correctamente después)
            .cors().disable(); // Deshabilita CORS (simple para prueba, configurar correctamente después)

        return http.build();
    }
}