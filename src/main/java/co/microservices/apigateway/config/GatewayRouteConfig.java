package co.microservices.apigateway.config;

import co.microservices.apigateway.AggregationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, AggregationFilter aggregationFilter) {
        return builder.routes()
                .route("equipos", r -> r.path("/equipos/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://equipo"))
                .route("entrenadores", r -> r.path("/entrenadores/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://entrenadores"))
                .route("miembros", r -> r.path("/miembros/**")
                        .filters(f -> f.rewritePath("/miembros/(?<rem>.*)", "/api/miembro/${rem}"))
                        .uri("lb://miembros"))
                .route("clase", r -> r.path("/clase/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://clase"))
                .route("resumen", r -> r.path("/resumen/")
                        .filters(f -> f.filter(aggregationFilter))
                        .uri("http://localhost:8087/"))
                .build();
    }
}
