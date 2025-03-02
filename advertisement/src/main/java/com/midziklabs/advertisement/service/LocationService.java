package com.midziklabs.advertisement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.repository.LocationRepository;
import com.midziklabs.advertisement.request.LocationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationModel saveLocation(LocationRequest request){
        return locationRepository.save(request.toLocationModel());
    }
    public List<LocationModel> getAllLocations(){
        return locationRepository.findAll();
    }
    public Optional<LocationModel> getLocationById(Long id){
        log.info("Location Service id received: "+id);
        return locationRepository.findById(id);
    }
    public LocationModel updateLocation(LocationRequest request, Long id){
        Optional<LocationModel> curr_location = locationRepository.findById(id);
        if(curr_location.isPresent()){
            curr_location.get().setAddress(request.getAddress());
            curr_location.get().setName(request.getName());
            curr_location.get().setCounty(request.getCounty());
            curr_location.get().setPrice(request.getPrice());
            return locationRepository.save(curr_location.get());
        } else {
            return null;
        }
    }
    public void deleteLocation(Long id){
        locationRepository.deleteById(id);
    }
}
