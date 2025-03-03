package com.midziklabs.analytics.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ViewCountAnalyticsModel {
    @JsonProperty(value = "view_count", defaultValue = "0")
    private Object view_count;
    @JsonProperty(value = "positive_sentiment", defaultValue = "0")
    private Object positive_sentiment;
    @JsonProperty(value = "negative_sentiment", defaultValue = "0")
    private Object negative_sentiment;
    @JsonProperty(value = "neutral_sentiment", defaultValue = "0")
    private Object neutral_sentiment;
}
