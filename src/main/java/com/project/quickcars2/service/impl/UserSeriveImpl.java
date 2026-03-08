package com.project.quickcars2.service.impl;

import com.project.quickcars2.entity.User;
import com.project.quickcars2.repository.UserRepository;
import com.project.quickcars2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSeriveImpl implements UserService {
    private final ModelMapper modelMapper;
    private  final UserRepository userRepo;



    @Override
    public void saveOrUpdateUser(String email, String name) {
        if (!userRepo.existsById(email)) {
            userRepo.save(new User(email, name));
        }
    }
}
