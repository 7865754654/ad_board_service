package com.example.jwt.converter.deserializer;

import com.example.jwt.models.Token;

public interface TokenDeserializer {
    Token deserialize(String jwt);
}
