package com.theatomicity.pdf.bill.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Company {
    @JsonProperty
    private String name;
    @JsonProperty
    private String status;
    @JsonProperty
    private List<String> address;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String website;
    @JsonProperty
    private String email;
    @JsonProperty
    private List<String> legal;


}
