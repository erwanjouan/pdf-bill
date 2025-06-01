package com.theatomicity.pdf.bill.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Template {
    @JsonProperty
    private String reference;
    @JsonProperty("from")
    private Company fromCompany;
    @JsonProperty("to")
    private Company toCompany;
    @JsonProperty
    private String jobTitle;
    @JsonProperty
    private String jobContent;
    @JsonProperty
    private BigDecimal vat;
    @JsonProperty
    private List<String> bankInfo;
    @JsonProperty
    private String disclaimer;
}
