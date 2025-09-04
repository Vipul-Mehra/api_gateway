package com.example.Centralized_db.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_id", "product_id"})
})
public class ProductRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @JsonIgnoreProperties("productRoles")
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties("productRoles")
    private Product product;
}
