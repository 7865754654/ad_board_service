package com.example.jwt.converter.serializer;

import com.example.jwt.models.Token;

public interface TokenSerializer {
    String serializer(Token token);
}
