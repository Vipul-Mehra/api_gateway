package com.example.gateway.repository;

import com.example.gateway.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsByClientClientNameAndProductProductName(String clientName, String productName);
}
