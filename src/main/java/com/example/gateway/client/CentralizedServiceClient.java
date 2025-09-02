package com.example.gateway.client;

import com.example.gateway.dto.ProductDto;
import com.example.gateway.dto.RealmDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class CentralizedServiceClient {

    private final WebClient webClient;

    public CentralizedServiceClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8087/api").build();
    }

    // Blocking calls for simplicity
    public List<ProductDto> fetchProducts() {
        return webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block();
    }

    public List<RealmDto> fetchRealms() {
        return webClient.get()
                .uri("/realms")
                .retrieve()
                .bodyToFlux(RealmDto.class)
                .collectList()
                .block();
    }
}
