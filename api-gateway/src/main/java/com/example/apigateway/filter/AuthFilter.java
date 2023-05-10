package com.example.apigateway.filter;

import com.google.common.net.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Value("${auth.hostname}")
    private String authHostname;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "un authorized access to application: missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                RestTemplate restTemplate = new RestTemplate();
                String url = String.format("http://%s:8081/api/auth/validate?token=", authHostname);
                ResponseEntity<String> response = restTemplate.getForEntity(url + authHeader, String.class);

                if(!(response.getBody() == null)) {
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("login", response.getBody())
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());

                }
                else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                            "un authorized access to application: token is not valid");
                }
        });
    }

    public static class Config {
    }

}
