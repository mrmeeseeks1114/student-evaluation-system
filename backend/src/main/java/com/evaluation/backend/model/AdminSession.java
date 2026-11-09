package com.evaluation.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_sessions")
public class AdminSession {

    @Id
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "admin_user_id", nullable = false)
    private Long adminUserId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public AdminSession() {
    }

    public AdminSession(String tokenHash, Long adminUserId, LocalDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.adminUserId = adminUserId;
        this.expiresAt = expiresAt;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
