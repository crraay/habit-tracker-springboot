package com.kostya.habittracker.entity;

import com.kostya.habittracker.enums.UserRole;
import com.kostya.habittracker.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="`user`")
@EqualsAndHashCode(callSuper = true)
public class User extends BasicAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(unique = true, nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserStatus status = UserStatus.PENDING_VERIFICATION;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role = UserRole.USER;

    @Column(nullable = false)
    Integer failedLoginAttempts = 0;

    LocalDateTime lastLoginAt;

    LocalDateTime passwordChangedAt;
}
