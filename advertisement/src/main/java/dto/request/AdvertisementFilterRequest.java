package dto.request;

import entity.Category;

import java.time.LocalDateTime;

public record AdvertisementFilterRequest(
        String name,
        Category category,
        String subcategory,
        Integer minCost,
        Integer maxCost,
        String address,
        LocalDateTime created
) {
}
