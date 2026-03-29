package dto.request;

import entity.*;

import java.time.LocalDateTime;


public record AdvertisementRequest(
        String name,
        Category category,
        String subcategory,
        //Image image,
        Condition condition,
        Availability availability,
        String description,
        Integer cost,
        String address,
        Long userId,
        Status status
) {}
