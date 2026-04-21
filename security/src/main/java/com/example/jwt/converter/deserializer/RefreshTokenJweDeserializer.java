package com.example.jwt.converter.deserializer;

import com.example.jwt.models.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;

@Component("refreshTokenDeserializer")
public class RefreshTokenJweDeserializer implements TokenDeserializer{

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweDeserializer(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }


    @Override
    public Token deserialize(String jwt) {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be null");
        }
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(jwt);
            encryptedJWT.decrypt(jweDecrypter);
            JWTClaimsSet claims = encryptedJWT.getJWTClaimsSet();
            return new Token(UUID.fromString(
                    claims.getJWTID()),
                    claims.getSubject(),
                    claims.getStringListClaim("authorities"),
                    claims.getIssueTime().toInstant(),
                    claims.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
