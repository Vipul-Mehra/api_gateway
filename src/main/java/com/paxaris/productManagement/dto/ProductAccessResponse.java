package com.paxaris.productManagement.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductAccessResponse {
    private final boolean allowed;
    private final String message;
    private final String productUrl;
    private final String productUri;
    private final List<String> roleName;

    public static ProductAccessResponse allowed(String message, String productUrl, String productUri, List<String> roleName) {
        return ProductAccessResponse.builder()
                .allowed(true)
                .message(message)
                .productUrl(productUrl)
                .productUri(productUri)
                .roleName(roleName)
                .build();
    }

    public static ProductAccessResponse denied(String message) {
        return ProductAccessResponse.builder()
                .allowed(false)
                .message(message)
                .build();
    }
}
