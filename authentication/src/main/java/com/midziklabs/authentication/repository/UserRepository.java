package com.midziklabs.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midziklabs.authentication.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByName(String name);
    Optional<UserModel> findByEmail(String email);

}
