package com.paxaris.productManagement.service;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import reactor.core.publisher.Flux;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

public interface DynamicRouteService extends RouteDefinitionLocator {
    Flux<RouteDefinition> getRouteDefinitions();
    void updateRoutes(List<RealmProductRoleUrl> entries);
}
