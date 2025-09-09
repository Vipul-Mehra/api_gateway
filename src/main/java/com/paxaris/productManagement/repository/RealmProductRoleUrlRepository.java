package com.paxaris.productManagement.repository;

import com.paxaris.productManagement.entities.RealmProductRoleUrl;
import com.paxaris.productManagement.compositeKey.RealmProductRoleUrlId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealmProductRoleUrlRepository extends JpaRepository<RealmProductRoleUrl, RealmProductRoleUrlId> {

    List<RealmProductRoleUrl> findByRealmName(String realmName);

    List<RealmProductRoleUrl> findByRealmNameAndProductNameAndRoleNameIn(
            String realmName, String productName, List<String> roleName
    );
}
