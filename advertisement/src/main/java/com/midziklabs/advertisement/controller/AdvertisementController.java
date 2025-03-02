package com.midziklabs.advertisement.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.request.AdvertisementRequest;
import com.midziklabs.advertisement.service.AdvertisementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/v1/advertisement")
@RequiredArgsConstructor
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping("/welcome")
    public ResponseEntity<String> getWelcomePage() {
        return ResponseEntity.ok().body("Hello World");
    }
    @PostMapping("/")
    public ResponseEntity<?> addAdvertisement(
        @RequestParam("title") String title,
        @RequestParam("description") String description, 
        @RequestParam("category_id") String category_id, 
        @RequestParam("location_ids") String location_ids,
        @RequestPart("visuals") MultipartFile visuals) {
        try {
            log.error("LOcation ids received: "+location_ids);
            AdvertisementRequest request = new AdvertisementRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setCategory_id(Long.valueOf(category_id));
            request.setVisuals(visuals);
            request.setLocation_ids(location_ids);
            AdvertisementModel savedAdverisement = advertisementService.addAdvertisement(request);
            URI location = URI.create(String.format("/api/v1/advertisement/%s", savedAdverisement.getId()));
            return ResponseEntity.created(location).body(savedAdverisement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred when trying to save the advertisement: "+e.getMessage());
        }
    }
    @GetMapping("/")
    public ResponseEntity<?> getAdvertisements() {
        try {
            List<AdvertisementModel> advertisement_list = advertisementService.getAllAdvertisements();
            if (advertisement_list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(advertisement_list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred when trying to get advertisements: "+e.getMessage());
        }
    }
    
    // TODO: Get Advertisement By Location/Screen
    @GetMapping("/location/id/{location_id}")
    public ResponseEntity getAdvertisementByLocationId(@PathVariable("location_id") Integer id) {
        return ResponseEntity.ok().body(advertisementService.getAdvertisementsByLocation(Long.valueOf(id)));
    }
    
    
    

}
