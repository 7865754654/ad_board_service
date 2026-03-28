package entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
    @Table(name = "advertisement")
    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public class Advertisement extends BaseEntity {

        @Column(name = "name", unique = true, nullable = false, length = 100)
        private String name;               // Название объявления

        @Enumerated(EnumType.STRING)
        @Column(name = "category", nullable = false, length = 100)
        private Category category;           // Категория

        @Column(name = "subcategory", nullable = false, length = 100)
        private String subcategory;        // Подкатегория

        @OneToOne
        @JoinColumn(name = "id_img", referencedColumnName = "id", nullable = false)
        private Image image;              // Фото объявления

        @Enumerated(EnumType.STRING)
        @Column(name = "condition", nullable = false, length = 50)
        private Condition condition;       // Состояние товара

        @Enumerated(EnumType.STRING)
        @Column(name = "availability", nullable = false, length = 50)
        private Availability availability; // Наличие

        @Column(name = "description", nullable = false, length = 625)
        private String description;        // Описание

        @Column(name = "cost", nullable = false)
        private Integer cost;              // Стоимость

        @Column(name = "address", nullable = false, length = 150)
        private String address;            // Адрес

        @Column(name = "id_user", nullable = false)
        private Long userId;                 // Автор объявления (берется email или номер)

    }


