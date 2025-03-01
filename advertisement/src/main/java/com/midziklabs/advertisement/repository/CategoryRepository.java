package com.midziklabs.advertisement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midziklabs.advertisement.model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    Optional<CategoryModel> findByName(String category_name);
}
