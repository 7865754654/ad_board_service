package com.example.dto.request;

import com.example.enums.Category;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementFilterRequest {
    private String name;
    private Category category;
    private String subcategory;
    @Positive private Integer minCost;
    @Positive private Integer maxCost;
    private String address;
    private LocalDateTime created;
}
