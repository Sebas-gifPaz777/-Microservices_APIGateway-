package co.microservices;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Anotación para indicar que esta clase es una fuente de definiciones de beans
@Configuration
public class GatewayRouteConfig {

    // Define un bean de tipo RouteLocator que Spring Cloud Gateway usará para obtener las rutas
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // --- Ruta para el Microservicio de Equipos ---
                // Basado en EquipoController @RequestMapping("/Equipos")
                .route("equipos-route-java", r -> r.path("/equipos/**") // Ruta externa que el cliente usa
                    .filters(f -> f
                        // Reescribe la ruta: quita /equipos/ y añade /Equipos/ (para coincidir con el controlador)
                        .rewritePath("/equipos/(?<remaining>.*)", "/Equipos/${remaining}")
                    )
                    // URI del microservicio de equipos (temporal - cambiará en Paso 4)
                    .uri("http://localhost:8081/")
                )

                // --- Ruta para el Microservicio de Entrenadores ---
                // Basado en EntrenadorController @RequestMapping("/")
                .route("entrenadores-route-java", r -> r.path("/entrenadores/**") // Ruta externa
                    .filters(f -> f
                        // Reescribe la ruta: quita /entrenadores/ y deja el resto (coincide con controlador en raíz)
                        .rewritePath("/entrenadores/(?<remaining>.*)", "/${remaining}")
                        // Nota: RewritePath aquí actúa como StripPrefix=1
                    )
                    // URI del microservicio de entrenadores (temporal)
                    .uri("http://localhost:8082/")
                )

                // --- Ruta para el Microservicio de Miembros ---
                // Basado en MiembroController @RequestMapping("/api/miembro")
                .route("miembros-route-java", r -> r.path("/miembros/**") // Ruta externa
                    .filters(f -> f
                        // Reescribe la ruta: quita /miembros/ y añade /api/miembro/ (coincide con controlador)
                        .rewritePath("/miembros/(?<remaining>.*)", "/api/miembro/${remaining}")
                    )
                    // URI del microservicio de miembros (temporal)
                    .uri("http://localhost:8083/")
                )

                // --- Ruta para el Microservicio de Clases ---
                // Asumimos un controlador con @RequestMapping("/") o similar que necesita StripPrefix
                .route("clase-route-java", r -> r.path("/clase/**") // Ruta externa
                    .filters(f -> f
                        // Reescribe la ruta: quita /clase/ y deja el resto
                        .rewritePath("/clase/(?<remaining>.*)", "/${remaining}")
                        // Nota: RewritePath aquí actúa como StripPrefix=1
                    )
                    // URI del microservicio de clases (temporal)
                    .uri("http://localhost:8084/")
                )

                .build(); // Finaliza la construcción de rutas
    }

}