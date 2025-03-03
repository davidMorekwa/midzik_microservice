package com.midziklabs.analytics.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.midziklabs.analytics.model.AgeAnalyticsModel;
import com.midziklabs.analytics.model.GenderAnalyticsModel;
import com.midziklabs.analytics.model.ViewCountAnalyticsModel;
import com.midziklabs.analytics.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/analytics/")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService firestoreService;

    @GetMapping("/gender/{id}")
    public ResponseEntity<?> getGenderAnalytics(@PathVariable("id") String ad_id) {
        GenderAnalyticsModel model = firestoreService.getGenderAnalytics(ad_id);
        return ResponseEntity.ok().body(model);
    }
    @GetMapping("/age/{id}")
    public ResponseEntity<?> getAgeAnalytics(@PathVariable("id") String ad_id) {
        AgeAnalyticsModel model = firestoreService.getAgeAnalytics(ad_id);
        return ResponseEntity.ok().body(model);
    }
    @GetMapping("/view_count/{id}")
    public ResponseEntity<?> getViewCOuntAnalytics(@PathVariable("id") String ad_id) {
        ViewCountAnalyticsModel model = firestoreService.getViewCountAnalytics(ad_id);
        return ResponseEntity.ok().body(model);
    }
    
    
    


}
