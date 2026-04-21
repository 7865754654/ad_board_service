package com.example.jwt.models;

public record Tokens(
        String accessToken,
        String accessTokenExpire,
        String refreshToken,
        String refreshTokenExpire
) { }
