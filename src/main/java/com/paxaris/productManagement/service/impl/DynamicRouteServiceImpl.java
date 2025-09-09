package com.paxaris.productManagement.service.impl;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.service.DynamicRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicRouteServiceImpl implements DynamicRouteService {

    private final List<RouteDefinition> routes = new ArrayList<>();
    private final Sinks.Many<List<RouteDefinition>> sink = Sinks.many().replay().latest();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        System.out.println("🔎 DynamicRouteService: Returning current route definitions");
        return sink.asFlux().flatMapIterable(list -> list);
    }

    @Override
    public void updateRoutes(List<RealmProductRoleUrl> entries) {
        System.out.printf("🔄 DynamicRouteService: Updating %d routes%n", entries.size());

        List<RouteDefinition> newRoutes = new ArrayList<>();
        for (RealmProductRoleUrl entry : entries) {
            RouteDefinition route = new RouteDefinition();
            route.setId(entry.getProductName() + "-service");
            route.setUri(URI.create(entry.getBaseUrl()));

            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setName("Path");
            predicate.setArgs(new HashMap<>() {{
                put("pattern", "/" + entry.getProductName() + "/**");
            }});

            route.setPredicates(List.of(predicate));
            newRoutes.add(route);

            System.out.printf("➡ Route added: ID=%s, URL=%s, Pattern=%s%n",
                    route.getId(), entry.getBaseUrl(), "/" + entry.getProductName() + "/**");
        }

        this.routes.clear();
        this.routes.addAll(newRoutes);
        sink.tryEmitNext(this.routes);

        System.out.printf("✅ DynamicRouteService: Total routes updated: %d%n", newRoutes.size());
    }
}
