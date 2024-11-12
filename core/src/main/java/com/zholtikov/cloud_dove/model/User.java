package com.zholtikov.cloud_dove.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zholtikov.cloud_dove.enums.UserRole;
import com.zholtikov.cloud_dove.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users", schema = "clouddove")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true, nullable = false)
    String email;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    @JsonIgnore
    String passwordHash;
    @Column(nullable = false)
    String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;

    public User() {
    }

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }


    public User(String email, String username, String passwordHash, String name, UserStatus status, UserRole role) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.status = status;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}