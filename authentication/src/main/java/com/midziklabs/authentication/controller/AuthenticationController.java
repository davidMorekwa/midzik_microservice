package com.midziklabs.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midziklabs.authentication.dto.LoginResponse;
import com.midziklabs.authentication.dto.LoginUserDto;
import com.midziklabs.authentication.dto.RegisterUserDto;
import com.midziklabs.authentication.model.RoleModel;
import com.midziklabs.authentication.model.UserModel;
import com.midziklabs.authentication.repository.RoleRepository;
import com.midziklabs.authentication.service.AuthenticationService;
import com.midziklabs.authentication.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;

    @GetMapping("/welcome")
    public String getWelcomePage() {
        return "Hello Weorld ffrom AuthenticationController";
    }
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('User')")
    public String getUser() {
        return "Hello User";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('Administrator')")
    public String getAdmin() {
        return "Hello Admin";
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@RequestBody RegisterUserDto registerUserDto) {
        UserModel registeredUser = authenticationService.register(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        UserModel authenticatedUSer = authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUSer);
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        response.setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/roles")
    public List<RoleModel> getRoles(@RequestParam String param) {
        return roleRepository.findAll();
    }
    
    
}
