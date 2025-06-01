package com.theatomicity.pdf.bill.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Job {
    private String start;
    private String end;
    private String title;
    private String description;
    private double dailyFee;
    private double vat;
}
