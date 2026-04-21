package com.example;

/**
 * Base entity for all domain objects.
 *
 * Contains common fields such as id, creation time, last update time,
 * and entity status.
 *
 * Uses Spring Data JPA auditing to automatically populate
 * created and updated fields.
 *
 * Marked as @MappedSuperclass, so its fields are inherited
 * by other entities without creating a separate table.
 */


import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor(force = true)
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created")
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "updated")
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    private Status status;
}