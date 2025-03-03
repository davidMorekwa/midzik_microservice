package com.midziklabs.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderAnalyticsModel {
    private Object male_count;
    private Object female_count;
}
