package com.example.Centralized_db.repository;

import com.example.Centralized_db.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Check if a realm (via client) is subscribed to a product
    boolean existsByClientRealmNameAndProductProductName(String realmName, String productName);
}
