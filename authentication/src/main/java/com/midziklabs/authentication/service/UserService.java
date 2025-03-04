package com.midziklabs.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.midziklabs.authentication.model.UserModel;
import com.midziklabs.authentication.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserModel> allUsers(){
        List<UserModel>  users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(user));
        return users;
    }

}
