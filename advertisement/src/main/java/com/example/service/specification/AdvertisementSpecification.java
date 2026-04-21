package com.example.service.specification;

import com.example.entity.Advertisement;
import com.example.dto.request.AdvertisementFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementSpecification {

    public static Specification<Advertisement> withFilter(AdvertisementFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по name
            if (filter.getName() != null && !filter.getName().isBlank()) {
                String pattern = "%" + filter.getName().toLowerCase() + "%";
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                pattern
                        )
                );
            }

            // Фильтр по category
            if (filter.getCategory() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("category"), filter.getCategory())
                );
            }

            // Фильтр по подкатегории
            if (filter.getSubcategory() != null && !filter.getSubcategory().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("subcategory"), filter.getSubcategory())
                );
            }

            // Фильтр по минимальной цене
            if (filter.getMinCost() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), filter.getMinCost())
                );
            }

            // Фильтр по максимальной цене
            if (filter.getMaxCost() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("cost"), filter.getMaxCost())
                );
            }

            // Фильтр по адресу
            if (filter.getAddress() != null && !filter.getAddress().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("address"), filter.getAddress())
                );
            }

            // Фильтр по дате создания
            if (filter.getCreated() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("created"), filter.getCreated())
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}