package com.example.entity;


import com.example.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "roles")
public class Role extends BaseEntity {
    @Column(name = "name")
    private  String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
}