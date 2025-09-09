package com.paxaris.productManagement.compositeKey;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RealmProductRoleUrlId implements Serializable {
    private String realmName;
    private String productName;
    private String roleName;
    private String baseUrl;
    private String uri;
}
