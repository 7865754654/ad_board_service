package com.example.config;

import com.example.jwt.converter.BasicAuthenticationConverter;
import com.example.jwt.converter.JwtConverter;
import com.example.jwt.converter.deserializer.TokenDeserializer;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    //access - serializer
    @Bean
    public JWSSigner jwsSigner()
            throws KeyLengthException {
        return new MACSigner(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public JWSAlgorithm jwsAlgorithm() {
        return JWSAlgorithm.HS256;
    }


    //access - deserializer
    @Bean
    public JWSVerifier jwsVerifier()
            throws JOSEException {
        return new MACVerifier(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    //refresh - serializer
    @Bean
    public JWEEncrypter jweEncrypter()
            throws KeyLengthException {
        return new DirectEncrypter(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));  // ← ключ здесь
    }

    @Bean
    public JWEAlgorithm jweAlgorithm() {
        return JWEAlgorithm.DIR;
    }

    @Bean
    public EncryptionMethod encryptionMethod() {
        return EncryptionMethod.A192CBC_HS384;
    }

    @Bean
    //refresh - deserializer
    public JWEDecrypter jweDecrypter()
            throws KeyLengthException {
        return new DirectDecrypter(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public JwtConverter jwtConverter(TokenDeserializer accessTokenJwsDeserializer) {
        return new JwtConverter(accessTokenJwsDeserializer);
    }
}
