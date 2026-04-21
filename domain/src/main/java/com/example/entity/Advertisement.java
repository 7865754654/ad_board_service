package com.example.entity;

import com.example.BaseEntity;
import com.example.enums.Availability;
import com.example.enums.Category;
import com.example.enums.Condition;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "advertisements")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class Advertisement extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 100)
    private Category category;

    @Column(name = "subcategory", nullable = false, length = 100)
    private String subcategory;

    //@OneToOne
    //@JoinColumn(name = "id_img", referencedColumnName = "id")
    //private Image image;


    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false, length = 50)
    private Condition condition;


    @Column(name = "description", nullable = false, length = 625)
    private String description;


    @Column(name = "cost", nullable = false)
    private Integer cost;


    @Column(name = "address", nullable = false, length = 150)
    private String address;

    @OneToMany(mappedBy = "advertisement", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}