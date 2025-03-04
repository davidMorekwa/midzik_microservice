package com.midziklabs.authentication.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.midziklabs.authentication.dto.LoginUserDto;
import com.midziklabs.authentication.dto.RegisterUserDto;
import com.midziklabs.authentication.model.RoleModel;
import com.midziklabs.authentication.model.UserModel;
import com.midziklabs.authentication.repository.RoleRepository;
import com.midziklabs.authentication.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserModel register(RegisterUserDto registerUserDto){
        UserModel user = new UserModel();
        if(registerUserDto.getRole_id() == null){
            log.info("Role id is null");
            Optional<RoleModel> role = roleRepository.findById(Long.valueOf(3));
            if(role.isPresent()){
                user.setRole(role.get());
            } else {
                log.error("ROle not found");
            }
        } else {
            RoleModel role = roleRepository.findById(Long.valueOf(registerUserDto.getRole_id())).orElseThrow();
            user.setRole(role);
        }
        user.setEmail(registerUserDto.getEmail());
        user.setName(registerUserDto.getName());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        return userRepository.save(user);
    }
    public UserModel login(LoginUserDto loginUserDto){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(), loginUserDto.getPassword())
        );
        return userRepository.findByEmail(loginUserDto.getEmail())
            .orElseThrow();
    }
    
}
