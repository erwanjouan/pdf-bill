package com.theatomicity.pdf.bill.model;

import com.theatomicity.pdf.bill.config.BillConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Bill {
    private BillConfig billConfig;
    private String reference;
    private String fullReference;
    private LocalDate date;
    private BigDecimal monthlyNetFee;
    private BigDecimal vatFee;
    private BigDecimal monthlyGrossFee;
    private String destinationFile;
}
