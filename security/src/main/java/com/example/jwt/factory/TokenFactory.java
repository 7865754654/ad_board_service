package com.example.jwt.factory;

import com.example.jwt.models.Token;

import java.time.Duration;

public interface TokenFactory<T> {
    Token create(T source);
    Duration LifeTime();
}
