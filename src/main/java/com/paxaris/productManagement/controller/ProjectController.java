package com.paxaris.productManagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paxaris.productManagement.dto.ProductAccessResponse;
import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.repository.RealmProductRoleUrlRepository;
import com.paxaris.productManagement.service.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final SyncService syncService;
    private final RealmProductRoleUrlRepository realmProductRoleUrlRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @PostMapping("/register")
    public ResponseEntity<ProductAccessResponse> registerProject(@RequestBody RealmProductRoleUrl entry) {
        try {
            log.info("📥 Received single role registration payload:\n{}",
                    objectMapper.writeValueAsString(entry));
        } catch (JsonProcessingException e) {
            log.error("❌ Failed to pretty-print request payload", e);
        }

        syncService.syncRolesFromGateway(List.of(entry));
        log.info("✅ Saved single role to DB: realm={}, product={}, role={}, url={}, uri={}",
                entry.getRealmName(),
                entry.getProductName(),
                entry.getRoleName(),
                entry.getUrl(),
                entry.getUri()
        );

        return ResponseEntity.ok(ProductAccessResponse.allowed(
                "✅ Project mapping saved to DB",
                entry.getUrl(),
                entry.getUri(),
                List.of(entry.getRoleName())
        ));
    }

    @PostMapping("/sync-roles")
    public ResponseEntity<ProductAccessResponse> syncRoles(@RequestBody List<RealmProductRoleUrl> roles) {
        try {
            log.info("📥 Received roles sync payload from API Gateway:\n{}",
                    objectMapper.writeValueAsString(roles));
        } catch (JsonProcessingException e) {
            log.error("❌ Failed to pretty-print request payload", e);
        }

        syncService.syncRolesFromGateway(roles);
        log.info("✅ All roles saved to DB (count={})", roles.size());

        return ResponseEntity.ok(ProductAccessResponse.allowed(
                "✅ Roles synced successfully from API Gateway",
                null,
                null,
                roles.stream().map(RealmProductRoleUrl::getRoleName).toList()
        ));
    }

    @PutMapping("/roles/{realm}/{client}/{roleName}")
    public ResponseEntity<String> updateRoleInDB(
            @PathVariable String realm,
            @PathVariable String client,
            @PathVariable String roleName,
            @RequestBody RealmProductRoleUrl roleUpdate) {
        try {
            int updated = realmProductRoleUrlRepository.updateRole(
                    realm,
                    client,
                    roleName,
                    roleUpdate.getUrl(),
                    roleUpdate.getUri()
            );

            if (updated > 0) {
                return ResponseEntity.ok("✅ Role updated in Project Manager DB");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("⚠️ Role not found in DB");
            }
        } catch (Exception e) {
            log.error("❌ Failed to update role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to update role");
        }
    }

    @DeleteMapping("/delete-role/{realm}/{client}/{roleName}")
    public ResponseEntity<String> deleteRole(
            @PathVariable String realm,
            @PathVariable String client,
            @PathVariable String roleName) {

        boolean deleted = syncService.deleteRoleFromDb(realm, client, roleName);

        if (deleted) {
            return ResponseEntity.ok("✅ Role deleted from DB");
        } else {
            return ResponseEntity.status(404).body("⚠️ Role not found in DB");
        }
    }
}
