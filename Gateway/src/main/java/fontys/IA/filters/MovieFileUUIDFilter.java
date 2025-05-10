package fontys.IA.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovieFileUUIDFilter extends AbstractGatewayFilterFactory<Object> {
    public MovieFileUUIDFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String movieId = UUID.randomUUID().toString();

            // Change the http request to add a new header, which is the movieId
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("movieId", movieId)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }
}


