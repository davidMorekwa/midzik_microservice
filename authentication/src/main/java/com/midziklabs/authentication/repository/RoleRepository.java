package com.midziklabs.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midziklabs.authentication.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(String name);
}
