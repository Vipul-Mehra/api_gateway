package com.example.gateway.service;

import com.example.gateway.entities.Product;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DynamicRouteService implements RouteDefinitionLocator {

    private final List<RouteDefinition> routes = new ArrayList<>();
    private final Sinks.Many<List<RouteDefinition>> sink = Sinks.many().replay().latest();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return sink.asFlux().flatMapIterable(list -> list);
    }

    public void updateRoutes(List<Product> products) {
        List<RouteDefinition> newRoutes = new ArrayList<>();

        for (Product product : products) {
            RouteDefinition route = new RouteDefinition();
            route.setId(product.getProductName() + "-service");
            route.setUri(URI.create(product.getProductUrl()));

            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setName("Path");
            predicate.setArgs(new HashMap<>() {{
                put("pattern", "/" + product.getProductName() + "/**");
            }});

            route.setPredicates(List.of(predicate));
            newRoutes.add(route);
        }

        this.routes.clear();
        this.routes.addAll(newRoutes);

        // notify Gateway
        sink.tryEmitNext(this.routes);
        System.out.println("✅ Routes updated: " + newRoutes.size());
    }
}
