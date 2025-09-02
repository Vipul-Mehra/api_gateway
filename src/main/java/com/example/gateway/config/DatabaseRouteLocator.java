package com.example.gateway.config;

import com.example.gateway.client.CentralizedServiceClient;
import com.example.gateway.dto.ProductDto;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseRouteLocator implements RouteDefinitionLocator {

    private final CentralizedServiceClient client;

    public DatabaseRouteLocator(CentralizedServiceClient client) {
        this.client = client;
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<ProductDto> products = client.fetchProducts();

        if (products.isEmpty()) {
            System.out.println("⚠️ No products available. Routes not loaded.");
            return Flux.empty();
        }

        List<RouteDefinition> routes = new ArrayList<>();
        for (ProductDto product : products) {
            RouteDefinition route = new RouteDefinition();
            route.setId(product.getProductName() + "-service");
            route.setUri(URI.create(product.getProductUrl()));

            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setName("Path");
            predicate.addArg("pattern", "/" + product.getProductName() + "/**");

            route.setPredicates(List.of(predicate));
            routes.add(route);
        }

        return Flux.fromIterable(routes);
    }
}
