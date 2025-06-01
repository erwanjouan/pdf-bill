package com.theatomicity.pdf.bill.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Config {
    @JsonProperty
    private BigDecimal manDays;
    @JsonProperty
    private int month;
    @JsonProperty
    private int year;
    @JsonProperty
    private Template template;
    @JsonProperty
    private String jobStart;
    @JsonProperty
    private String jobEnd;
    @JsonProperty
    private BigDecimal dailyFee;
}
