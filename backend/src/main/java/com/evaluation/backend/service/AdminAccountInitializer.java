package com.evaluation.backend.service;

import com.evaluation.backend.model.AdminUser;
import com.evaluation.backend.repository.AdminSessionRepository;
import com.evaluation.backend.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private final AdminUserRepository adminUserRepository;
    private final AdminSessionRepository adminSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public AdminAccountInitializer(
        AdminUserRepository adminUserRepository,
        AdminSessionRepository adminSessionRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.username:}") String username,
        @Value("${app.admin.password:}") String password
    ) {
        this.adminUserRepository = adminUserRepository;
        this.adminSessionRepository = adminSessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (username.isBlank() && password.isBlank()) {
            return;
        }
        if (username.isBlank() || password.isBlank()) {
            throw new IllegalStateException(
                "Configure both ADMIN_USERNAME and ADMIN_PASSWORD before starting the Java backend."
            );
        }
        if (
            username.equals("change-this-username")
            || password.equals("replace-with-a-long-unique-password")
        ) {
            return;
        }

        List<AdminUser> existingAdmins = adminUserRepository.findAll();
        AdminUser admin = existingAdmins.stream()
            .filter(existingAdmin -> existingAdmin.getUsername().equals(username.trim()))
            .findFirst()
            .orElseGet(() -> existingAdmins.stream().findFirst().orElseGet(AdminUser::new));

        adminSessionRepository.deleteAllInBatch();
        adminUserRepository.deleteAll(existingAdmins.stream()
            .filter(existingAdmin -> !existingAdmin.getId().equals(admin.getId()))
            .toList());
        admin.setUsername(username.trim());
        admin.setPasswordHash(passwordEncoder.encode(password));
        adminUserRepository.save(admin);
    }
}
