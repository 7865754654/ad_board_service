package com.example.jwt.factory;

import com.example.jwt.models.Token;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component("accessTokenFactory")
public class AccessTokenFactory  implements TokenFactory<Token>{

    @Override
    public Token create(Token refreshToken) {
        Instant now = Instant.now();

        List<String> authorities =  refreshToken.authorities()
                .stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();

        return new Token(
                UUID.randomUUID(),
                refreshToken.subject(),
                authorities,
                now,
                now.plus(LifeTime())
        );
    }

    @Override
    public Duration LifeTime() {
        return Duration.ofMinutes(5);
    }
}
