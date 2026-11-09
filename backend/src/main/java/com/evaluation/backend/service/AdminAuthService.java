package com.evaluation.backend.service;

import com.evaluation.backend.model.AdminSession;
import com.evaluation.backend.model.AdminUser;
import com.evaluation.backend.repository.AdminSessionRepository;
import com.evaluation.backend.repository.AdminUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AdminAuthService {

    private static final int TOKEN_BYTES = 32;
    private static final int SESSION_HOURS = 8;

    private final AdminUserRepository adminUserRepository;
    private final AdminSessionRepository adminSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public AdminAuthService(
        AdminUserRepository adminUserRepository,
        AdminSessionRepository adminSessionRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.adminUserRepository = adminUserRepository;
        this.adminSessionRepository = adminSessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean isConfigured() {
        return adminUserRepository.count() > 0;
    }

    @Transactional
    public Optional<String> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }

        Optional<AdminUser> admin = adminUserRepository.findByUsername(username.trim());
        if (admin.isEmpty() || !passwordEncoder.matches(password, admin.get().getPasswordHash())) {
            return Optional.empty();
        }

        LocalDateTime now = LocalDateTime.now();
        adminSessionRepository.deleteByExpiresAtBefore(now);

        byte[] tokenBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);

        adminSessionRepository.save(new AdminSession(
            hashToken(token),
            admin.get().getId(),
            now.plusHours(SESSION_HOURS)
        ));
        return Optional.of(token);
    }

    @Transactional(readOnly = true)
    public boolean isAuthenticated(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token == null) {
            return false;
        }

        return adminSessionRepository.findById(hashToken(token))
            .map(session -> session.getExpiresAt().isAfter(LocalDateTime.now()))
            .orElse(false);
    }

    @Transactional
    public void logout(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token != null) {
            adminSessionRepository.deleteById(hashToken(token));
        }
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        if (token.length() != 43 || !token.matches("[A-Za-z0-9_-]+")) {
            return null;
        }
        return token;
    }

    private String hashToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable.", exception);
        }
    }
}
