package com.example.jwt.converter.deserializer;

import com.example.jwt.models.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;

@Component("accessTokenDeserializer")
@Primary
public class AccessTokenJwsDeserializer implements TokenDeserializer{
    private final JWSVerifier jwsVerifier;

    public AccessTokenJwsDeserializer(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public Token deserialize(String jwt) {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("Access token cannot be null");
        }
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            if (signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return new Token(
                        UUID.fromString(claims.getJWTID()),
                        claims.getSubject(),
                        claims.getStringListClaim("authorities"),
                        claims.getIssueTime().toInstant(),
                        claims.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
