package com.theatomicity.pdf.bill.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Company {
    private String name;
    private String status;
    private List<String> address;
    private String phone;
    private String website;
    private String email;
    private List<String> legal;
}
