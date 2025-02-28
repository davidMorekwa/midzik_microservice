package com.midziklabs.advertisement.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.midziklabs.advertisement.model.AdvertisementModel;
import com.midziklabs.advertisement.request.AdvertisementRequest;
import com.midziklabs.advertisement.service.AdvertisementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
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
    public ResponseEntity<?> addAdvertisement(@RequestBody AdvertisementRequest request) {
        try {
            log.info("Request received: "+request.toString());
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
            List<AdvertisementModel> advertisement_list = advertisementService.getAdvertisements();
            if (advertisement_list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(advertisement_list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred when trying to get advertisements: "+e.getMessage());
        }
    }
    
    
    

}
