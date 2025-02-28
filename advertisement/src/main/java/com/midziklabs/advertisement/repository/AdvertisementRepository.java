package com.midziklabs.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.midziklabs.advertisement.model.AdvertisementModel;

public interface AdvertisementRepository extends JpaRepository<AdvertisementModel, Long> {
}
