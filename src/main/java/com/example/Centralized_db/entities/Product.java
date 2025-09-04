package com.example.Centralized_db.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, unique = true, length = 150)
    private String productName;

    @Column(name = "product_url", nullable = false, length = 255)
    private String productUrl;

    @Column(columnDefinition = "TEXT")
    private String description; // NEW field
}
