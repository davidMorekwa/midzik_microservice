package com.midziklabs.analytics.job;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgeAnalyticsJob {

    private Firestore firestore = FirestoreClient.getFirestore();

    private QuerySnapshot getData() {
        try {
            log.info("----------FETCHING AGE DATA FROM FIREBASE------------");
            CollectionReference collection_ref = firestore.collection("detection_data");
            log.info("Collection reference");
            log.info(collection_ref.count().toString());
            Query query = collection_ref.whereNotEqualTo("age_classification", "null");
            ApiFuture<QuerySnapshot> future = query.get();
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to get gender data: {}", e.getMessage());
            return QuerySnapshot.withDocuments(null, null, null);
        }
    }

    @Scheduled(cron = "0 */30 1 * * *")
    private void analyseAgeData() {
        log.info("------------PROCESSING AGE DATA FROM FIREBASE---------------");
        Map<String, Map<String, Integer>> age_classification_map = new HashMap<>();
        QuerySnapshot q_snapshot = getData();
        if (!q_snapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : q_snapshot) {
                Map<String, Object> doc_data = document.getData();
                String ad_id = doc_data.get("adId").toString();
                String age_class = doc_data.get("age_classification").toString();
                if (!age_classification_map.containsKey(ad_id)) {
                    Map<String, Integer> age_count = new HashMap<>();
                    /**
                     * "4-6 (early childhood)",
                     * "7-8 (middle childhood)",
                     * "9-11 (late childhood)",
                     * "12-19 (adolescent)",
                     * "20-27 (early adulthood)",
                     * "28-35 (middle adulthood)",
                     * "36-45 (midlife)",
                     * "46-60 (mature adulthood)",
                     * "61-100 (senior)"
                     */
                    switch (age_class) {
                        case "4-6 (early childhood)":
                            age_count.put("4-6 (early childhood)",
                                    age_count.getOrDefault("4-6 (early childhood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "7-8 (middle childhood)":
                            age_count.put("7-8 (middle childhood)",
                                    age_count.getOrDefault("7-8 (middle childhood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "9-11 (late childhood)":
                            age_count.put("9-11 (late childhood)",
                                    age_count.getOrDefault("9-11 (late childhood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "12-19 (adolescent)":
                            age_count.put("12-19 (adolescent)",
                                    age_count.getOrDefault("12-19 (adolescent)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "20-27 (early adulthood)":
                            age_count.put("20-27 (early adulthood))",
                                    age_count.getOrDefault("20-27 (early adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "28-35 (middle adulthood)":
                            age_count.put("28-35 (middle adulthood)",
                                    age_count.getOrDefault("28-35 (middle adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "36-45 (midlife)":
                            age_count.put("36-45 (midlife)",
                                    age_count.getOrDefault("36-45 (midlife)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "46-60 (mature adulthood)":
                            age_count.put("46-60 (mature adulthood)",
                                    age_count.getOrDefault("46-60 (mature adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        case "61-100 (senior)":
                            age_count.put("61-100 (senior)",
                                    age_count.getOrDefault("61-100 (senior)", 0) + 1);
                            age_classification_map.put(ad_id, age_count);
                            break;
                        default:
                            log.error("Unidentified classification");
                            break;
                    }
                } else {
                    Map<String, Integer> curr_age_count = age_classification_map.get(ad_id);
                    log.info("Age classification map");
                    log.info(curr_age_count.keySet().toString());
                    log.info("Age classification ");
                    switch (age_class) {
                        case "4-6 (early childhood)":
                            curr_age_count.put("4-6 (early childhood)",
                                    curr_age_count.getOrDefault("4-6 (early childhood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "7-8 (middle childhood)":
                            curr_age_count.put("7-8 (middle childhood)",
                                    curr_age_count.getOrDefault("7-8 (middle childhood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "9-11 (late childhood)":
                            curr_age_count.put("9-11 (late childhood)",
                                    curr_age_count.getOrDefault("9-11 (late childhood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "12-19 (adolescent)":
                            curr_age_count.put("12-19 (adolescent)",
                                    curr_age_count.getOrDefault("12-19 (adolescent)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "20-27 (early adulthood)":
                            curr_age_count.put("20-27 (early adulthood)",
                                    curr_age_count.getOrDefault("20-27 (early adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "28-35 (middle adulthood)":
                            curr_age_count.put("28-35 (middle adulthood)",
                                    curr_age_count.getOrDefault("28-35 (middle adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "36-45 (midlife)":
                            curr_age_count.put("36-45 (midlife)",
                                    curr_age_count.getOrDefault("36-45 (midlife)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "46-60 (mature adulthood)":
                            curr_age_count.put("46-60 (mature adulthood)",
                                    curr_age_count.getOrDefault("46-60 (mature adulthood)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        case "61-100 (senior)":
                            curr_age_count.put("61-100 (senior)",
                                    curr_age_count.getOrDefault("61-100 (senior)", 0) + 1);
                            age_classification_map.put(ad_id, curr_age_count);
                            break;
                        default:
                            log.error("Unidentified classification");
                            break;
                    }
                }
            }
        }
        saveAgeAnalytics(age_classification_map);
    }

    private void saveAgeAnalytics(Map<String, Map<String, Integer>> age_classification_map) {
        log.info("-------------------------SAVING PROCESSED AGE DATA TO FIREBASE--------------------");
        try {
            Set<String> key_set = age_classification_map.keySet();
            for (String key : key_set) {
                ApiFuture<WriteResult> writeFuture = firestore.collection("age_classification").document(key)
                        .set(age_classification_map.get(key));
                log.info("Age analytics saved to firebase");
            }
        } catch (Exception e) {
            log.error("Error saving age analytics: {}", e.getMessage());
        }
    }

}
