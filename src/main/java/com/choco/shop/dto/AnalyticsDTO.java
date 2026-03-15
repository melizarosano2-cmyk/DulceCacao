package com.choco.shop.dto;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsDTO {
    private List<String> labels;
    private List<BigDecimal> values;
    private String label;

    public AnalyticsDTO(List<String> labels, List<BigDecimal> values, String label) {
        this.labels = labels;
        this.values = values;
        this.label = label;
    }

    // Getters and Setters
    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<BigDecimal> getValues() {
        return values;
    }

    public void setValues(List<BigDecimal> values) {
        this.values = values;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
