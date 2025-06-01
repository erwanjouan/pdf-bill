package com.theatomicity.pdf.bill.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.layout.Document;
import com.theatomicity.pdf.bill.config.BillConfig;
import com.theatomicity.pdf.bill.config.LabelsConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import com.theatomicity.pdf.bill.section.content.BillDetailTable;
import com.theatomicity.pdf.bill.section.content.BillFromTo;
import com.theatomicity.pdf.bill.section.content.BillTotalTable;
import com.theatomicity.pdf.bill.section.layout.BillFooter;
import com.theatomicity.pdf.bill.section.layout.BillHeader;
import com.theatomicity.pdf.bill.section.lower.BillLegalQuote;
import com.theatomicity.pdf.bill.section.upper.BillDate;
import com.theatomicity.pdf.bill.section.upper.BillReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class Controller implements Section {

    @Autowired
    private BillHeader billHeader;

    @Autowired
    private BillReference billReference;

    @Autowired
    private BillDate billDate;

    @Autowired
    private BillFromTo billFromTo;

    @Autowired
    private BillDetailTable billDetailTable;

    @Autowired
    private BillTotalTable billTotalTable;

    @Autowired
    private BillLegalQuote billLegalQuote;

    @Autowired
    private BillFooter billFooter;

    @Autowired
    private Document document;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LabelsConfig labelsConfig;

    @Autowired
    private Path outputFolder;

    public void render(final Bill bill) {
        List.of(
                this.billHeader,
                this.billReference,
                this.billDate,
                this.billFromTo,
                this.billDetailTable,
                this.billTotalTable,
                this.billLegalQuote,
                this.billFooter
        ).forEach(section -> section.render(bill));
        this.document.close();
    }

    public File getFile(final Bill bill) {
        final String tmpFile = this.labelsConfig.getOutput().getTmpFile();
        final String folder = this.outputFolder.toString();
        final Path source = Paths.get(folder, tmpFile);
        final String pattern = this.labelsConfig.getOutput().getPattern();
        final String fromCompany = bill.getBillConfig().getFrom().getName().replace(" ", "");
        final String toCompany = bill.getBillConfig().getTo().getName().replace(" ", "");
        final String destFileName = String.format(
                pattern,
                fromCompany,
                toCompany,
                bill.getBillConfig().getYear(),
                bill.getBillConfig().getMonth());
        final Path dest = Paths.get(folder, destFileName);
        try {
            if (Boolean.TRUE.equals(Files.exists(dest))) {
                Files.deleteIfExists(dest);
            }
            final Path destPath = Files.move(source, dest);
            return destPath.toFile();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Bill getBillFromJson(final String inputFile) {
        try {
            final String json = Files.readString(Paths.get(inputFile));
            final BillConfig billConfig = this.objectMapper.readValue(json, BillConfig.class);
            final Bill bill = new Bill();
            bill.setBillConfig(billConfig);
            this.setDate(bill);
            this.setReference(bill);
            this.setFullReference(bill);
            this.setMonthlyNetFee(bill);
            this.setVatFee(bill);
            this.setMonthlyGrossFee(bill);
            this.setDestinationFile(bill, this.outputFolder);
            return bill;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMonthlyGrossFee(final Bill bill) {
        final BigDecimal vatFee = bill.getVatFee();
        final BigDecimal monthlyNetFee = bill.getMonthlyNetFee();
        final BigDecimal monthlyGrossFee = vatFee.add(monthlyNetFee);
        bill.setMonthlyGrossFee(monthlyGrossFee);
    }

    private void setVatFee(final Bill bill) {
        final BigDecimal monthlyNetFee = bill.getMonthlyNetFee();
        final BigDecimal vatPercentage = BigDecimal.valueOf(bill.getBillConfig().getJob().getVat());
        final BigDecimal vatFee = monthlyNetFee.multiply(vatPercentage).divide(BigDecimal.valueOf(100.00), RoundingMode.HALF_DOWN);
        bill.setVatFee(vatFee);
    }

    private void setMonthlyNetFee(final Bill bill) {
        final BigDecimal manDays = BigDecimal.valueOf(bill.getBillConfig().getManDays());
        final BigDecimal dailyFee = BigDecimal.valueOf(bill.getBillConfig().getJob().getDailyFee());
        final BigDecimal netMonthlyFee = manDays.multiply(dailyFee);
        bill.setMonthlyNetFee(netMonthlyFee);
    }

    private void setDate(final Bill bill) {
        final int year = bill.getBillConfig().getYear();
        final int month = bill.getBillConfig().getMonth();
        final LocalDate lastDayOfMonth = this.getLastDayOfMonth(year, month);
        bill.setDate(lastDayOfMonth);
    }


    private void setReference(final Bill bill) {
        final int year = bill.getBillConfig().getYear();
        final int month = bill.getBillConfig().getMonth();
        final int referenceYear = year - 2023;
        final int refInt = referenceYear * 12 + month;
        final String reference = String.format("%03d", refInt);
        bill.setReference(reference);
    }

    private void setFullReference(final Bill bill) {
        final String reference = bill.getReference();
        final int year = bill.getBillConfig().getYear() - 2000;
        final int month = bill.getBillConfig().getMonth();
        final String fullReference = String.format("F%02d-%02d/%s", month, year, reference);
        bill.setFullReference(fullReference);
    }

    private LocalDate getLastDayOfMonth(final int year, final int month) {
        final LocalDate localDate = LocalDate.of(year, month, 1);
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    private void setDestinationFile(final Bill bill, final Path outputFolder) {
        final String pattern = this.labelsConfig.getOutput().getPattern();
        final String destFileName = String.format(pattern,
                bill.getBillConfig().getFrom().getName(),
                bill.getBillConfig().getTo().getName(),
                bill.getBillConfig().getYear(),
                bill.getBillConfig().getMonth());
        final Path dest = Paths.get(outputFolder.toString(), destFileName);
        bill.setDestinationFile(dest.toString());
    }

}
