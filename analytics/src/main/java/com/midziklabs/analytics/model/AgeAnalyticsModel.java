package com.midziklabs.analytics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeAnalyticsModel {
    @JsonProperty(value = "4-6 (early childhood)", defaultValue = "0")
    private Object early_childhood;
    @JsonProperty(value = "7-8 (middle childhood)", defaultValue = "0")
    private Object middle_childhood;
    @JsonProperty(value = "9-11 (late childhood)", defaultValue = "0")
    private Object late_childhood;
    @JsonProperty(value = "12-19 (adolescent)", defaultValue = "0")
    private Object adolescent;
    @JsonProperty(value = "20-27 (early adulthood)", defaultValue = "0")
    private Object early_adulthood;
    @JsonProperty(value = "28-35 (middle adulthood)", defaultValue = "0")
    private Object middle_adulthood;
    @JsonProperty(value = "36-45 (midlife)", defaultValue = "0")
    private Object midlife;
    @JsonProperty(value = "46-60 (mature adulthood)", defaultValue = "0")
    private Object mature_adulthood;
    @JsonProperty(value = "61-100 (senior)", defaultValue = "0")
    private Object senior;
}
