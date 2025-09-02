package com.example.gateway.security;

import com.example.gateway.client.CentralizedServiceClient;
import com.example.gateway.dto.RealmDto;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class DynamicJwtDecoderFactory {

    private final CentralizedServiceClient client;
    private final Map<String, ReactiveJwtDecoder> cache = new ConcurrentHashMap<>();

    public DynamicJwtDecoderFactory(CentralizedServiceClient client) {
        this.client = client;
    }

    public ReactiveJwtDecoder getDecoder(String issuer) {
        return cache.computeIfAbsent(issuer, iss -> {
            List<RealmDto> realms = client.fetchRealms();
            for (RealmDto realm : realms) {
                if (realm.getIssuerUri().equals(iss)) {
                    String jwkSetUri = realm.getIssuerUri() + "/protocol/openid-connect/certs";
                    return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
                }
            }

            return token -> reactor.core.publisher.Mono.error(
                    new IllegalArgumentException("Unknown issuer: " + iss)
            );
        });
    }
}
