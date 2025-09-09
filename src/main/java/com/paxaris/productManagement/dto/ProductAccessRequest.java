package com.paxaris.productManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductAccessRequest {
    private String username;  // optional (can be ignored in DB check)
    private String realm;
    private String product;
    @JsonProperty("roles")
    private List<String> roleName;
}
