package service.specification;

import dto.request.AdvertisementFilterRequest;
import entity.Advertisement;
import entity.Advertisement_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementSpecification {
    public static Specification<Advertisement> withFilter(
            AdvertisementFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по name
            if (filter.name() != null && !filter.name().isBlank()) {
                String pattern = "%" + filter.name()
                        .toLowerCase()
                        .replace("_", "\\_")
                        .replace("%", "\\%") + "%";

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(Advertisement_.name)),
                                pattern,
                                '\\'
                        )
                );
            }

            // Фильтр по category
            if (filter.category() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(Advertisement_.category),
                                filter.category()
                        )
                );
            }

// Фильтр по подкатегории
            if (filter.subcategory()!= null && !filter.subcategory().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(Advertisement_.subcategory),
                                filter.subcategory()
                        )
                );
            }

//  Фильтр по минимальной цене
            if (filter.minCost() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(Advertisement_.cost),
                                filter.minCost()
                        )
                );
            }

//  Фильтр по максимальной цене
            if (filter.maxCost() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(Advertisement_.cost),
                                filter.maxCost()
                        )
                );
            }

//  Фильтр по адресу
            if (filter.address() != null && !filter.address().isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(Advertisement_.address),
                                filter.address()
                        )
                );
            }

// Фильтр по дате создания
            if (filter.created() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(Advertisement_.created),
                                filter.created()
                        )
                );
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}