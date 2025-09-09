package com.paxaris.productManagement.controller;

import com.paxaris.productManagement.dto.ProductAccessRequest;
import com.paxaris.productManagement.dto.ProductAccessResponse;
import com.paxaris.productManagement.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @PostMapping("/check")
    public ResponseEntity<ProductAccessResponse> checkAccess(@RequestBody ProductAccessRequest request) {
        System.out.printf("🔎 CHECK ACCESS: username=%s, realm=%s, product=%s, role=%s%n",
                request.getUsername(), request.getRealm(), request.getProduct(), request.getRoleName());

        return ResponseEntity.ok(accessService.checkAccess(request));
    }
}
