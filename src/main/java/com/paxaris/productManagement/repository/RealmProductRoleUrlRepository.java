package com.paxaris.productManagement.repository;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.compositeKey.RealmProductRoleUrlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RealmProductRoleUrlRepository extends JpaRepository<RealmProductRoleUrl, RealmProductRoleUrlId> {

    List<RealmProductRoleUrl> findByRealmName(String realmName);

    List<RealmProductRoleUrl> findByRealmNameAndProductNameAndRoleNameIn(
            String realmName,
            String productName,
            List<String> roles
    );

    @Modifying
    @Transactional
    @Query("UPDATE RealmProductRoleUrl r SET r.url = :url, r.uri = :uri " +
            "WHERE r.realmName = :realm AND r.productName = :client AND r.roleName = :roleName")
    int updateRole(@Param("realm") String realm,
                   @Param("client") String client,
                   @Param("roleName") String roleName,
                   @Param("url") String url,
                   @Param("uri") String uri);
}
