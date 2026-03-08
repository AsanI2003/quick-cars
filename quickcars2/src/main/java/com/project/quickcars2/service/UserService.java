package com.project.quickcars2.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public void saveOrUpdateUser(String email, String name);
}
