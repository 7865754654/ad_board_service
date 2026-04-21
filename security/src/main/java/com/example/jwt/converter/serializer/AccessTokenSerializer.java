package com.example.jwt.converter.serializer;

import com.example.jwt.models.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component("accessTokenSerializer")
public class AccessTokenSerializer implements TokenSerializer {

    private final JWSSigner jwsSigner;
    private final JWSAlgorithm jwsAlgorithm;

    public AccessTokenSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }


    @Override
    public String serializer(Token token) {
        JWSHeader header = new JWSHeader.Builder(jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .claim("authorities", token.authorities())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .build();


        SignedJWT signedJWT = new SignedJWT(header, claims);
        try {
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Access token serialization failed", e);
        }
    }
}












