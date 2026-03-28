package dto.response;

import entity.*;

import java.time.LocalDateTime;


public record AdvertisementResponse(
        String name,
        Category category,
        String subcategory,
        Image image,
        Condition condition,
        Availability availability,
        Integer cost,
        String address,
        String description,
        Long userId,
        Status status,
        LocalDateTime created
) {}
