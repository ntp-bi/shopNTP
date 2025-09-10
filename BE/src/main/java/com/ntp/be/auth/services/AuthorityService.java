package com.ntp.be.auth.services;

import com.ntp.be.auth.entities.Authority;
import com.ntp.be.auth.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    // Lấy quyền USER mặc định
    public List<Authority> getUserAuthorities() {
        Authority authority = authorityRepository.findByRoleCode("ROLE_USER");
        if (authority == null) {
            authority = createAuthority("ROLE_USER", "Default user role");
        }
        return Collections.singletonList(authority);
    }

    // Lấy quyền ADMIN mặc định
    public List<Authority> getAdminAuthorities() {
        Authority authority = authorityRepository.findByRoleCode("ROLE_ADMIN");
        if (authority == null) {
            authority = createAuthority("ROLE_ADMIN", "Default admin role");
        }
        return Collections.singletonList(authority);
    }

    // Tạo quyền mới (nếu chưa có)
    public Authority createAuthority(String role, String description) {
        Authority existing = authorityRepository.findByRoleCode(role);
        if (existing != null) {
            return existing;
        }
        Authority authority = Authority.builder()
                .roleCode(role)
                .roleDescription(description)
                .build();
        return authorityRepository.save(authority);
    }

    // Lấy tất cả quyền
    public Page<Authority> getAllAuthorities(Pageable pageable) {
        return authorityRepository.findAll(pageable);
    }
}

