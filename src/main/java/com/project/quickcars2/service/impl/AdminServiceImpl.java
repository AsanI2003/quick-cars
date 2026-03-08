package com.project.quickcars2.service.impl;

import com.project.quickcars2.entity.Admin;
import com.project.quickcars2.repository.AdminRepository;
import com.project.quickcars2.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public Admin save(Admin admin) {
        admin.setPassword(encoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username).orElse(null);
    }

    public boolean checkPassword(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
