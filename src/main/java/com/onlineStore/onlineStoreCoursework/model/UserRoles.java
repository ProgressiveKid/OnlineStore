package com.onlineStore.onlineStoreCoursework.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_roles")
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

}
