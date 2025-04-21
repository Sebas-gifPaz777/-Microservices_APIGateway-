package co.microservices.apigateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AggregationFilter implements GatewayFilter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (true) {
            return aggregateResponses(exchange);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> aggregateResponses(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString(); // /resumen/123
        String[] segments = path.split("/");
        if (segments.length < 3) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        String miembroId = segments[2];
        Mono<List<Object>> clases = webClientBuilder.build().get()
                .uri("http://localhost:8084/api/clase/listar")
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList();

        Mono<Object> miembro = webClientBuilder.build().get()
                .uri("http://localhost:8083/api/miembro/get/"+miembroId)
                .retrieve()
                .bodyToMono(Object.class);

        return Mono.zip(clases, miembro).flatMap(tuple -> {
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("miembro", tuple.getT2());
            resumen.put("clases", tuple.getT1());

            try {
                byte[] json = new ObjectMapper().writeValueAsBytes(resumen);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return exchange.getResponse().writeWith(Mono.just(buffer));
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            }
        });
    }
}