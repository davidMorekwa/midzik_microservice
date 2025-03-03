package com.midziklabs.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionDataModel {
    private Integer adId;
    private String age_classification;
    private Double eulerX;
    private Double eulerY;
    private Double eulerZ;
    private String gender_classification;
    private Double left_eye_open_probability;
    private Double right_eye_open_probability;
    private Double smiling_probability;
    private Integer tracking_id;

}
