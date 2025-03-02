package com.midziklabs.advertisement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midziklabs.advertisement.model.LocationModel;
import com.midziklabs.advertisement.request.LocationRequest;
import com.midziklabs.advertisement.service.LocationService;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/")
    public ResponseEntity<?> addLocation(@RequestBody LocationRequest request) {
        LocationModel new_location = locationService.saveLocation(request);
        URI location = URI.create(String.format("/api/v1/location/id/%s", new_location.getId()));
        return ResponseEntity.created(location).body(new_location);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllLocations() {
        List<LocationModel> location_list = locationService.getAllLocations();
        return ResponseEntity.ok().body(location_list);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Integer id) {
        Optional<LocationModel> location = locationService.getLocationById(Long.valueOf(id));
        if(location.isPresent()){
            return ResponseEntity.ok().body(location.get());
        } else {
            return ResponseEntity.badRequest().body("Location with id "+id+" doesn't exist");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable("id") Integer id, @RequestBody LocationRequest request) {
        LocationModel updated_location = locationService.updateLocation(request, Long.valueOf(id));
        if(updated_location != null){
            return ResponseEntity.ok().body(updated_location);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred when trying to update the location data");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Long id){
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }
    

    

}
