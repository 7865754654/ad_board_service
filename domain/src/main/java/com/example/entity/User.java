package com.example.entity;


import com.example.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", unique = true)
    private String phone;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Advertisement> advertisements = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="user_roles",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>();
}