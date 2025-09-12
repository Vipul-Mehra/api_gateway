package com.paxaris.productManagement.entities;

import com.paxaris.productManagement.compositeKey.RealmProductRoleUrlId;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "realm_product_role_url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RealmProductRoleUrlId.class)
public class RealmProductRoleUrl {

    @Id
    @Column(name = "realm_name", length = 50, nullable = false)
    private String realmName;

    @Id
    @Column(name = "product_name", length = 50, nullable = false)
    private String productName;

    @Id
    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Column(name = "base_url", length = 191, nullable = false)
    private String url;

    @Column(name = "uri", length = 191)
    private String uri;
}
