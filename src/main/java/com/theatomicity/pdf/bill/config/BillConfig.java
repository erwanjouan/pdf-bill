package com.theatomicity.pdf.bill.config;

import com.theatomicity.pdf.bill.model.Company;
import com.theatomicity.pdf.bill.model.Job;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BillConfig {
    private int month;
    private int year;
    private double manDays;
    private Company from;
    private Company to;
    private Job job;
    private List<String> bankInfo;
    private String disclaimer;
}
