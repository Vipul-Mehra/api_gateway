package com.example.Centralized_db.controller;

import com.example.Centralized_db.entities.Client;
import com.example.Centralized_db.entities.Product;
import com.example.Centralized_db.entities.Role;
import com.example.Centralized_db.entities.User;
import com.example.Centralized_db.repository.ClientRepository;
import com.example.Centralized_db.repository.ProductRepository;
import com.example.Centralized_db.repository.SubscriptionRepository;
import com.example.Centralized_db.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    // ===================== Product Access Check =====================
    @PostMapping("/check-access")
    public ResponseEntity<ProductAccessResponse> checkAccess(@RequestBody ProductAccessRequest request) {

        // 1. Lookup user by username + realm
        Optional<User> userOpt = userRepository.findByUsernameAndRealmName(request.getUsername(), request.getRealm());
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(
                    ProductAccessResponse.denied("❌ User not found in DB for realm=" + request.getRealm())
            );
        }
        User user = userOpt.get();

        // 2. Check roles for requested product
        Optional<Role> roleOpt = user.getRoles().stream()
                .filter(role -> role.getProductUri() != null
                        && role.getProductUri().contains(request.getProduct()))
                .findFirst();

        if (roleOpt.isEmpty()) {
            return ResponseEntity.ok(
                    ProductAccessResponse.denied("❌ User does not have role for product=" + request.getProduct())
            );
        }
        Role role = roleOpt.get();

        // 3. Lookup Client by realm
        Client client = clientRepository.findByRealmName(request.getRealm())
                .orElseThrow(() -> new RuntimeException("❌ Client not found for realm=" + request.getRealm()));

        // 4. Lookup Product by product name
        Product product = productRepository.findByProductName(request.getProduct())
                .orElseThrow(() -> new RuntimeException("❌ Product not found: " + request.getProduct()));

        // 5. Verify subscription exists for client + product
        boolean subscribed = subscriptionRepository.existsByClientAndProduct(client, product);

        if (!subscribed) {
            return ResponseEntity.ok(
                    ProductAccessResponse.denied("❌ Realm=" + request.getRealm() + " is not subscribed to product=" + request.getProduct())
            );
        }

        // ✅ Success
        System.out.printf("✅ Access granted: user=%s, realm=%s, product=%s, role=%s%n",
                request.getUsername(), request.getRealm(), request.getProduct(), role.getRoleName());

        return ResponseEntity.ok(
                ProductAccessResponse.allowed(
                        "✅ Access granted",
                        product.getProductUrl(),
                        role.getProductUri(),
                        role.getRoleName()
                )
        );
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
        private String message;
        private String productUrl;
        private String productUri;
        private String roleName;

        public static ProductAccessResponse allowed(String message, String productUrl, String productUri, String roleName) {
            ProductAccessResponse res = new ProductAccessResponse();
            res.allowed = true;
            res.message = message;
            res.productUrl = productUrl;
            res.productUri = productUri;
            res.roleName = roleName;
            return res;
        }

        public static ProductAccessResponse denied(String message) {
            ProductAccessResponse res = new ProductAccessResponse();
            res.allowed = false;
            res.message = message;
            return res;
        }
    }
}
