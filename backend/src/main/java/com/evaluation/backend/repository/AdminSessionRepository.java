package com.evaluation.backend.repository;

import com.evaluation.backend.model.AdminSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AdminSessionRepository extends JpaRepository<AdminSession, String> {

    void deleteByExpiresAtBefore(LocalDateTime expirationTime);
}
