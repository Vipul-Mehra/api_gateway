package com.example.gateway.controller;

import com.example.gateway.entities.Product;
import com.example.gateway.entities.Role;
import com.example.gateway.entities.User;
import com.example.gateway.repository.UserRepository;
import com.example.gateway.service.DynamicRouteService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {

    private final UserRepository userRepository;

    // ===================== Product Access Check =====================
    @PostMapping("/check-access")
    public ResponseEntity<ProductAccessResponse> checkAccess(@RequestBody ProductAccessRequest request) {

        Optional<User> userOpt = userRepository.findById(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(new ProductAccessResponse(false, null, null));
        }

        User user = userOpt.get();

        // Check if user has role for the requested product
        Optional<Role> roleOpt = user.getRoles().stream()
                .filter(role -> role.getProductUri() != null && role.getProductUri().contains(request.getProduct()))
                .findFirst();

        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            return ResponseEntity.ok(new ProductAccessResponse(true,
                    null,  // If you have product URL, you can add here
                    role.getProductUri()));
        } else {
            return ResponseEntity.ok(new ProductAccessResponse(false, null, null));
        }
    }


    // ===================== Request / Response DTOs =====================
    @Data
    public static class ProductAccessRequest {
        private String username;
        private String realm;
        private String product;
    }

    @Data
    public static class ProductAccessResponse {
        private boolean allowed;
        private String productBaseUrl;
        private String productUri;

        public ProductAccessResponse(boolean allowed, String productBaseUrl, String productUri) {
            this.allowed = allowed;
            this.productBaseUrl = productBaseUrl;
            this.productUri = productUri;
        }
    }
}
