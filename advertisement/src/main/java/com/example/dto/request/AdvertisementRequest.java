package com.example.dto.request;

import com.example.enums.Category;
import com.example.enums.Condition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record AdvertisementRequest(
        @NotBlank String name,
        @NotNull Category category,
        @NotBlank String subcategory,
        //Image image,
        @NotNull Condition condition,
        @NotBlank String description,
        @Positive @NotNull Integer cost,
        @NotBlank String address

) {}
