package com.theatomicity.pdf.bill.presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Config;
import com.theatomicity.pdf.bill.view.View;
import com.theatomicity.pdf.bill.view.ViewImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

import static com.theatomicity.pdf.bill.model.Constants.CONFIG_YML;
import static com.theatomicity.pdf.bill.model.Constants.FILE_PATTERN;
import static com.theatomicity.pdf.bill.model.Constants.OUTPUT_FOLDER;

public class Presenter {

    private final View view;
    private final Config config;

    public Presenter() {
        this.config = this.getConfig();
        this.view = new ViewImpl(this.config);
    }

    private Config getConfig() {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            final URI uri = this.getClass().getClassLoader().getResource(CONFIG_YML).toURI();
            final String yamlSource = new String(Files.readAllBytes(Paths.get(uri)), StandardCharsets.UTF_8);
            return mapper.readValue(yamlSource, Config.class);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public File generate() throws DocumentException, FileNotFoundException {
        final BigDecimal manDays = this.getManDays();
        final Bill bill = Bill.builder()
                .mois(this.getMonth())
                .annee(this.getYear())
                .manDays(manDays)
                .build();
        final YearMonth yearMonth = YearMonth.of(bill.getAnnee(), bill.getMois());
        bill.setYearMonth(yearMonth);
        bill.setDate(this.getLastDayOfMonth(yearMonth));
        bill.setReference(this.getReference(yearMonth));
        bill.setFullReference(this.getFullReference(yearMonth, bill.getReference()));
        bill.setPeriodFr(this.getPeriodFr(yearMonth));
        final BigDecimal dailyFee = this.config.getDailyFee().setScale(2, RoundingMode.HALF_UP);
        bill.setMonthlyNetFee(dailyFee.multiply(bill.getManDays()));
        final BigDecimal vat = this.config.getTemplate().getVat().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal vatRatio = vat.divide(new BigDecimal(100), RoundingMode.HALF_UP);
        bill.setVatFee(bill.getMonthlyNetFee().multiply(vatRatio));
        bill.setMonthlyGrossFee(bill.getMonthlyNetFee().add(bill.getVatFee()));
        //
        final File file = new File(this.getOutputFolder(), this.getFileName(bill));
        final Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        this.view.generate(bill, document);
        return file;
    }

    protected int getYear() {
        return this.config.getYear();
    }

    protected int getMonth() {
        return this.config.getMonth();
    }

    protected BigDecimal getManDays() {
        return this.config.getManDays().setScale(2, RoundingMode.HALF_UP);
    }

    protected String getOutputFolder() {
        return OUTPUT_FOLDER;
    }

    private String getPeriodFr(final YearMonth yearMonth) {
        final Date convertedFromYearMonth =
                Date.from(yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final SimpleDateFormat fr = new SimpleDateFormat("MMMM yyyy", new Locale("fr"));
        return fr.format(convertedFromYearMonth);
    }

    private String getFullReference(final YearMonth yearMonth, final String reference) {
        final int year = yearMonth.getYear() - 2000;
        final int month = yearMonth.getMonth().getValue();
        return String.format("F%02d-%02d/%s", month, year, reference);
    }

    private String getReference(final YearMonth yearMonth) {
        final int year = yearMonth.getYear() - 2023;
        final int month = yearMonth.getMonth().getValue();
        final int refInt = year * 12 + month;
        return String.format("%03d", refInt);
    }

    private LocalDate getLastDayOfMonth(final YearMonth yearMonth) {
        final LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth().getValue(), 1);
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    private String getFileName(final Bill bill) {
        final String from = this.config.getTemplate().getFromCompany().getName().replace(" ", "");
        final String to = this.config.getTemplate().getToCompany().getName().replace(" ", "");
        return String.format(FILE_PATTERN, from, to, bill.getAnnee(), bill.getMois());
    }
}
