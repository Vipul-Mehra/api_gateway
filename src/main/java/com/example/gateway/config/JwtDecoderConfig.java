package com.example.gateway.config;

import com.example.gateway.security.DynamicJwtDecoderFactory;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

@Configuration
public class JwtDecoderConfig {

    private final DynamicJwtDecoderFactory factory;

    public JwtDecoderConfig(DynamicJwtDecoderFactory factory) {
        this.factory = factory;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return token -> {
            try {
                SignedJWT parsed = SignedJWT.parse(token);
                String issuer = parsed.getJWTClaimsSet().getIssuer();
                if (issuer == null) return Mono.error(new IllegalArgumentException("Missing 'iss' claim"));

                return factory.getDecoder(issuer).decode(token);
            } catch (Exception e) {
                return Mono.error(new IllegalArgumentException("Invalid JWT", e));
            }
        };
    }
}
