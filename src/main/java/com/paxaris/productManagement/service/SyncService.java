package com.paxaris.productManagement.service;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.compositeKey.RealmProductRoleUrlId;
import com.paxaris.productManagement.repository.RealmProductRoleUrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncService {

    private final RealmProductRoleUrlRepository repository;

    @Transactional
    public void syncRolesFromGateway(List<RealmProductRoleUrl> roles) {
        log.info("📥 SyncService: Starting sync of {} roles", roles.size());

        for (RealmProductRoleUrl role : roles) {
            RealmProductRoleUrlId id = new RealmProductRoleUrlId(
                    role.getRealmName(),
                    role.getProductName(),
                    role.getRoleName()
            );

            if (!repository.existsById(id)) {
                repository.save(role);
                log.info("✅ New role synced: realm={}, product={}, role={}, url={}, uri={}",
                        role.getRealmName(),
                        role.getProductName(),
                        role.getRoleName(),
                        role.getUrl(),
                        role.getUri()
                );
            } else {
                log.debug("ℹ️ Role already exists, skipping: realm={}, product={}, role={}",
                        role.getRealmName(),
                        role.getProductName(),
                        role.getRoleName()
                );
            }
        }

        log.info("🏁 SyncService: Completed syncing {} roles", roles.size());
    }
}
