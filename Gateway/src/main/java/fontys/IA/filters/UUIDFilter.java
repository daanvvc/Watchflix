package fontys.IA.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDFilter extends AbstractGatewayFilterFactory<Object> {
    public UUIDFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String uuid = UUID.randomUUID().toString();

            // Change the http request to add a new header, which is the movieId
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("UUID", uuid)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        };
    }
}


