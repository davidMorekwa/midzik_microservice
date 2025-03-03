package com.midziklabs.analytics.job;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ViewCountJob {
    private Firestore firestore = FirestoreClient.getFirestore();

    private QuerySnapshot getData() {
        log.info("----------FETCHING VIEW COUNT DATA FROM FIREBASE------------");
        try {
            CollectionReference collection_ref = firestore.collection("detection_data");
            Query query = collection_ref.whereGreaterThan("eulerX", -10).whereLessThan("eulerX", 10)
                    .whereGreaterThan("eulerY", -10).whereLessThan("eulerY", 10)
                    .whereGreaterThan("eulerZ", -5).whereLessThan("eulerZ", 5);
            ApiFuture<QuerySnapshot> future = query.get();
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("An error occurred when trying to get view count data: {}", e.getMessage());
            return QuerySnapshot.withDocuments(null, null, null);
        }
    }

    @Scheduled(cron = "0 */30 1 * * *")
    private void analyeViewCountData() {
        log.info("------------PROCESSING VIEW COUNT DATA FROM FIREBASE---------------");
        Map<String, Map<String, Integer>> view_counnt_map = new HashMap<>();
        QuerySnapshot q_snapshot = getData();
        if (!q_snapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : q_snapshot) {
                Map<String, Object> doc_data = document.getData();
                String ad_id = doc_data.get("adId").toString();
                if (!view_counnt_map.containsKey(ad_id)) {
                    Map<String, Integer> view_count = new HashMap<>();
                    view_count.put("view_count", view_count.getOrDefault("view_count", 0) + 1);
                    Double smiling_probability = (Double) doc_data.get("smiling_probability");
                    if (smiling_probability >= 0.7) {
                        view_count.put("positive_sentiment", view_count.getOrDefault("positive_sentiment", 0) + 1);
                    } else if (smiling_probability >= 0.3 && smiling_probability < 0.7) {
                        view_count.put("neutral_sentiment", view_count.getOrDefault("neutral_sentiment", 0) + 1);
                    } else if (smiling_probability < 0.3) {
                        view_count.put("negative_sentiment", view_count.getOrDefault("negative_sentiment", 0) + 1);
                    } else {
                        log.error("Incorrect value in view_count");
                    }
                    view_counnt_map.put(ad_id, view_count);
                } else {
                    Map<String, Integer> curr_view_count = view_counnt_map.get(ad_id);
                    curr_view_count.put("view_count", curr_view_count.get("view_count") + 1);
                    Double smiling_probability = (Double) doc_data.get("smiling_probability");
                    if (smiling_probability >= 0.7) {
                        curr_view_count.put("positive_sentiment",
                                curr_view_count.getOrDefault("positive_sentiment", 0) + 1);
                    } else if (smiling_probability >= 0.3 && smiling_probability < 0.7) {
                        curr_view_count.put("neutral_sentiment",
                                curr_view_count.getOrDefault("neutral_sentiment", 0) + 1);
                    } else if (smiling_probability < 0.3) {
                        curr_view_count.put("negative_sentiment",
                                curr_view_count.getOrDefault("negative_sentiment", 0) + 1);
                    } else {
                        log.error("Incorrect value in view_count");
                    }
                    view_counnt_map.put(ad_id, curr_view_count);
                }
            }
        }
        saveViewCountAnalytics(view_counnt_map);
    }

    private void saveViewCountAnalytics(Map<String, Map<String, Integer>> view_count_map) {
        log.info("-----------------SAVING PROCESSED VIEW COUNT DATA TO FIREBASE----------------------");
        try {
            Set<String> key_set = view_count_map.keySet();
            for (String key : key_set) {
                ApiFuture<WriteResult> writeFuture = firestore.collection("view_count").document(key)
                        .set(view_count_map.get(key));
                log.info("Gender analytics saved with ID: {}", writeFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error saving gender analytics: {}", e.getMessage());
        }

    }
}
