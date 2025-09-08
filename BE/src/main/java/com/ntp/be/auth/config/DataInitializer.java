package com.ntp.be.auth.config;

import com.ntp.be.auth.entities.Authority;
import com.ntp.be.auth.entities.User;
import com.ntp.be.auth.repositories.UserDetailRepository;
import com.ntp.be.auth.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tạo ROLE_USER và ROLE_ADMIN nếu chưa có
        authorityService.createAuthority("ROLE_USER", "Default role for users");
        authorityService.createAuthority("ROLE_ADMIN", "Administrator role");

        // Nếu chưa có admin thì tạo
        if (userDetailRepository.findByEmail("admin@gmail.com") == null) {
            // Lấy ROLE_ADMIN từ DB (JPA quản lý entity này)
            List<Authority> adminAuthorities = authorityService.getAdminAuthorities();

            User admin = User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .firstName("Admin")
                    .lastName("System")
                    .phoneNumber("0123456789")
                    .enabled(true)
                    .provider("manual")
                    .authorities(adminAuthorities) // gán ROLE_ADMIN đã lấy từ DB
                    .build();

            userDetailRepository.save(admin);
            System.out.println("✅ Admin account created: admin@gmail.com / 123456");
        } else {
            System.out.println("ℹ️ Admin account already exists.");
        }
    }
}
