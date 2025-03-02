package com.midziklabs.advertisement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.midziklabs.advertisement.model.AdvertisementModel;
import java.util.List;
import com.midziklabs.advertisement.model.LocationModel;
import java.util.Set;

public interface AdvertisementRepository extends JpaRepository<AdvertisementModel, Long> {
    //
    @Query("SELECT s FROM AdvertisementModel s JOIN s.location c WHERE c.id = :location_id")
    List<AdvertisementModel> findByLocation(@Param("location_id") Long id);
}
