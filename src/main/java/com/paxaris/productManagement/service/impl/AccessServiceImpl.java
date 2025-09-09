package com.paxaris.productManagement.service.impl;

import com.paxaris.productManagement.dto.ProductAccessRequest;
import com.paxaris.productManagement.dto.ProductAccessResponse;
import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.repository.RealmProductRoleUrlRepository;
import com.paxaris.productManagement.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final RealmProductRoleUrlRepository repository;

    @Override
    public ProductAccessResponse checkAccess(ProductAccessRequest request) {
        System.out.printf("🔎 AccessService: Checking access for realm=%s, product=%s, roles=%s, username=%s%n",
                request.getRealm(), request.getProduct(), request.getRoleName(), request.getUsername());

        List<String> roles = request.getRoleName();

        // 🔹 If admin role is present → allow everything
        if (roles != null && roles.contains("admin")) {
            System.out.printf("✅ Admin access: %s can access ANYTHING in product=%s%n",
                    request.getUsername(), request.getProduct());

            // Optionally fetch product entries so admin still gets a valid redirect
            List<RealmProductRoleUrl> allProductEntries =
                    repository.findByRealmName(request.getRealm());

            if (!allProductEntries.isEmpty()) {
                RealmProductRoleUrl firstEntry = allProductEntries.get(0);
                return ProductAccessResponse.allowed(
                        "✅ Admin access granted",
                        firstEntry.getBaseUrl(),
                        firstEntry.getUri(),
                        List.of("admin")
                );
            } else {
                return ProductAccessResponse.allowed(
                        "✅ Admin access granted (no specific product URL in DB)",
                        null,
                        null,
                        List.of("admin")
                );
            }
        }

        // 🔹 Normal role-based check
        List<RealmProductRoleUrl> entries = repository.findByRealmNameAndProductNameAndRoleNameIn(
                request.getRealm(),
                request.getProduct(),
                roles
        );

        if (!entries.isEmpty()) {
            RealmProductRoleUrl urlEntry = entries.get(0);
            System.out.printf("✅ AccessService: Access granted for %s/%s/%s → URL=%s, URI=%s%n",
                    urlEntry.getRealmName(), urlEntry.getProductName(), urlEntry.getRoleName(),
                    urlEntry.getBaseUrl(), urlEntry.getUri());

            return ProductAccessResponse.allowed(
                    "✅ Access granted",
                    urlEntry.getBaseUrl(),
                    urlEntry.getUri(),
                    List.of(urlEntry.getRoleName())
            );
        } else {
            System.out.printf("❌ AccessService: Access denied for realm=%s, product=%s, roles=%s%n",
                    request.getRealm(), request.getProduct(), request.getRoleName());

            return ProductAccessResponse.denied("❌ Access denied: No matching role found");
        }
    }
}
