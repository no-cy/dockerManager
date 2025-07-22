package com.example.dockermanager.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {

    @Id
    @Column(name = "id")
    long userId;

    @Column
    String name;

    @Column
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    protected User() {}
}
