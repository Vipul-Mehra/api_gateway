package com.example.Centralized_db.controller;

import com.example.Centralized_db.entities.Product;
import com.example.Centralized_db.service.DynamicRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final DynamicRouteService dynamicRouteService;

    // ===================== Dynamic Route Registration =====================
    @PostMapping("/register")
    public ResponseEntity<String> registerRoutes(@RequestBody List<Product> products) {
        dynamicRouteService.updateRoutes(products);
        return ResponseEntity.ok("Routes registered successfully");
    }
}
