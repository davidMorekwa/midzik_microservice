package com.midziklabs.analytics.job;

import java.nio.file.DirectoryStream.Filter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.midziklabs.analytics.model.GenderAnalyticsModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GenderAnalyticsJob {

    private Firestore firestore = FirestoreClient.getFirestore();

    private QuerySnapshot getData() {
        try {
            log.info("----------FETCHING GENDER DATA FROM FIREBASE------------");
            CollectionReference collection_ref = firestore.collection("detection_data");
            log.info("Collection reference");
            log.info(collection_ref.count().toString());
            Query query = collection_ref.whereNotEqualTo("gender_classification", "null").limit(10);
            ApiFuture<QuerySnapshot> future = query.get();
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to get gender data: {}", e.getMessage());
            return QuerySnapshot.withDocuments(null, null, null);
        }
    }

    @Scheduled(cron = "0 0 */2 * * *")
    private void analyseGenderData() {
        log.info("------------PROCESSING GENDER DATA FROM FIREBASE---------------");
        Map<String, Map<String, Integer>> gender_classification_map = new HashMap<>();
        QuerySnapshot q_Snapshot = getData();
        if (!q_Snapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : q_Snapshot) {
                log.info("document id: " + document.getId());
                Map<String, Object> doc_data = document.getData();
                String ad_id = doc_data.get("adId").toString();
                if (!gender_classification_map.containsKey(ad_id)) {
                    // ad_id gender_count mapping doesn't exist
                    Map<String, Integer> gender_count = new HashMap<>();
                    if (doc_data.get("gender_classification").equals("Male")) {
                        gender_count.put("Male", 1);
                        gender_count.put("Female", 0);
                        gender_classification_map.put(ad_id, gender_count);
                    } else if (doc_data.get("gender_classification").equals("Female")) {
                        gender_count.put("Male", 0);
                        gender_count.put("Female", 1);
                        gender_classification_map.put(ad_id, gender_count);
                    }
                } else {
                    // ad_id gender_count mappinng exists
                    Map<String, Integer> curr_gender_count = gender_classification_map.get(ad_id);
                    if (doc_data.get("gender_classification").equals("Male")) {
                        Integer curr_male_count = curr_gender_count.get("Male");
                        curr_gender_count.put("Male", ++curr_male_count);
                        gender_classification_map.put(ad_id, curr_gender_count);
                    } else if (doc_data.get("gender_classification").equals("Female")) {
                        Integer curr_female_count = curr_gender_count.get("Female");
                        curr_gender_count.put("Female", ++curr_female_count);
                        gender_classification_map.put(ad_id, curr_gender_count);
                    }
                }
            }
            saveGenderAnalytics(gender_classification_map);
        }

    }

    private void saveGenderAnalytics(Map<String, Map<String, Integer>> genderClassificationMap) {
        log.info("-----------------SAVING PROCESSED GENDER DATA TO FIREBASE----------------------");
        try {
            Set<String> key_set = genderClassificationMap.keySet();
            for (String key : key_set) {
                ApiFuture<WriteResult> writeFuture = firestore.collection("gender_classification").document(key)
                        .set(genderClassificationMap.get(key));
                log.info("Gender analytics saved with ID: {}", writeFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving gender analytics: {}", e.getMessage());
        }

    }

}
