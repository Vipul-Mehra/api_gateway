package com.example.Centralized_db.repository;

import com.example.Centralized_db.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    // ✅ New multi-tenant safe lookup
    Optional<User> findByUsernameAndRealmName(String username, String realmName);
}
