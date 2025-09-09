package com.paxaris.productManagement.controller;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.service.DynamicRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final DynamicRouteService dynamicRouteService;

    @GetMapping
    public Flux<RouteDefinition> getRoutes() {
        return dynamicRouteService.getRouteDefinitions();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerRoutes(@RequestBody List<RealmProductRoleUrl> entries) {
        dynamicRouteService.updateRoutes(entries);
        return ResponseEntity.ok("✅ Routes registered successfully: " + entries.size());
    }
}
