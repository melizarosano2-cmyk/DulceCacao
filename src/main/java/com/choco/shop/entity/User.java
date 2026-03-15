package com.choco.shop.entity;

import javax.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    private String fullName;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    private String verificationToken;

    private String resetToken;

    private java.time.LocalDateTime resetTokenExpiry;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
