package com.project.quickcars2.service;

import com.project.quickcars2.entity.Admin;

public interface AdminService {
    Admin save(Admin admin);
    Admin findByUsername(String username);
}
