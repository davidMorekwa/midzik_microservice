package com.midziklabs.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.midziklabs.advertisement.model.LocationModel;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {
    
}
