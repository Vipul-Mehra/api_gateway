package com.example.Centralized_db.repository;

import com.example.Centralized_db.entities.Client;
import com.example.Centralized_db.entities.Product;
import com.example.Centralized_db.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // Correct version → expects actual entity objects
    boolean existsByClientAndProduct(Client client, Product product);
}
