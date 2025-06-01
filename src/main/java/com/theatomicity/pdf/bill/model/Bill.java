package com.theatomicity.pdf.bill.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
public class Bill {
    private YearMonth yearMonth;
    private int mois;
    private int annee;
    private String periodFr;
    private String reference;
    private String fullReference;
    private LocalDate date;
    private BigDecimal manDays;
    private BigDecimal monthlyNetFee;
    private BigDecimal vatFee;
    private BigDecimal monthlyGrossFee;
}
