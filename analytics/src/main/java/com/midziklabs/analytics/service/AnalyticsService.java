package com.midziklabs.analytics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Document;
import com.midziklabs.analytics.model.AgeAnalyticsModel;
import com.midziklabs.analytics.model.DetectionDataModel;
import com.midziklabs.analytics.model.GenderAnalyticsModel;
import com.midziklabs.analytics.model.ViewCountAnalyticsModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnalyticsService {
    Firestore firestore = FirestoreClient.getFirestore();
    
    public GenderAnalyticsModel getGenderAnalytics(String id){    
        try {
            CollectionReference collection_ref = firestore.collection("gender_classification");
            DocumentReference doc_ref = collection_ref.document(id);
            ApiFuture<DocumentSnapshot> future = doc_ref.get();
            DocumentSnapshot doc_snapshot = future.get();
            if (doc_snapshot.exists()) {
                Map<String, Object> doc_data = doc_snapshot.getData();
                GenderAnalyticsModel model = new GenderAnalyticsModel();
                model.setFemale_count(doc_data.get("Female"));
                model.setMale_count(doc_data.get("Male"));
                return model;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to fetch gender analytics data", e.getMessage());
            log.error("error", e);
            return null;
        }   
    }
    public AgeAnalyticsModel getAgeAnalytics(String id){
        try {
            ApiFuture<DocumentSnapshot> future = firestore.collection("age_classification").document(id).get();
            DocumentSnapshot doc_snapshot = future.get();
            if(doc_snapshot.exists()){
                Map<String, Object> doc_data = doc_snapshot.getData();
                Set<String> key_set = doc_data.keySet();
                for (String key : key_set) {
                    log.info("Key: "+key+"---------value: "+doc_data.get(key));
                }
                ObjectMapper objectMapper = new ObjectMapper();
                AgeAnalyticsModel model = objectMapper.convertValue(doc_data, AgeAnalyticsModel.class);
                return model;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to fetch age analytics data", e.getMessage());
            return null;
        }
    }
    public ViewCountAnalyticsModel getViewCountAnalytics(String id){
        try {
            ApiFuture<DocumentSnapshot> future = firestore.collection("view_count").document(id).get();
            DocumentSnapshot doc_snapshot = future.get();
            if (doc_snapshot.exists()) {
                Map<String, Object> doc_data = doc_snapshot.getData();
                Set<String> key_set = doc_data.keySet();
                for (String key : key_set) {
                    log.info("Key: " + key + "---------value: " + doc_data.get(key));
                }
                ObjectMapper objectMapper = new ObjectMapper();
                ViewCountAnalyticsModel model = objectMapper.convertValue(doc_data, ViewCountAnalyticsModel.class);
                return model;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to fetch view count analytics data", e.getMessage());
            return null;
        }
    }
}
