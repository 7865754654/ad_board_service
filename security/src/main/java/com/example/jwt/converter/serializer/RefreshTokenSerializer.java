package com.example.jwt.converter.serializer;

import com.example.jwt.models.Token;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component("refreshTokenSerializer")
public class RefreshTokenSerializer implements TokenSerializer {

    private final JWEEncrypter jweEncrypter;
    private final JWEAlgorithm jweAlgorithm;
    private final EncryptionMethod encryptionMethod;

    public RefreshTokenSerializer(
            JWEEncrypter jweEncrypter,
            JWEAlgorithm jweAlgorithm,
            EncryptionMethod encryptionMethod
    ) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String serializer(Token token) {
        try {
            JWEHeader header = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                    .keyID(token.id().toString())
                    .build();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .jwtID(token.id().toString())
                    .subject(token.subject())
                    .claim("authorities", token.authorities())
                    .issueTime(Date.from(token.createdAt()))
                    .expirationTime(Date.from(token.expiresAt()))
                    .build();

            EncryptedJWT encryptedJWT = new EncryptedJWT(header, claims);
            encryptedJWT.encrypt(jweEncrypter);

            String result = encryptedJWT.serialize();

            return result;

        } catch (JOSEException e) {
            throw new RuntimeException("Refresh token serialization failed", e);
        }
    }
}
