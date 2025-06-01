package com.theatomicity.pdf.bill.section.content;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.theatomicity.pdf.bill.config.LabelsConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import com.theatomicity.pdf.bill.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import static com.theatomicity.pdf.bill.model.Constants.HEADER_COLOR;

@Component
public class BillDetailTable implements Section {

    @Autowired
    private Document document;

    @Autowired
    private Utils utils;

    @Autowired
    private LabelsConfig labelsConfig;

    @Override
    public void render(final Bill bill) {
        final float[] columnWidths = {100, 300, 100, 100};
        final Table table = new Table(columnWidths);
        this.addTableHeader(table);
        this.addRows(table, bill);
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginTop(30);
        this.document.add(table);
    }

    private void addTableHeader(final Table table) {
        this.addHeaderCell(table, this.labelsConfig.getDetailLabels().getRef());
        this.addHeaderCell(table, this.labelsConfig.getDetailLabels().getContent());
        this.addHeaderCell(table, this.labelsConfig.getDetailLabels().getQuantity());
        this.addHeaderCell(table, this.labelsConfig.getDetailLabels().getUnitPrice());
    }

    private void addHeaderCell(final Table table, final String label) {
        final Cell header = new Cell();
        header.setBackgroundColor(HEADER_COLOR);
        header.add(this.utils.getSmall(label));
        header.setPadding(5);
        table.addCell(header);
    }

    private void addRows(final Table table, final Bill bill) {
        table.addCell(this.addRef(bill));
        table.addCell(this.addPrestation(bill));
        table.addCell(this.addQuantite(bill));
        table.addCell(this.addPuHt(bill));
    }

    private Cell addQuantite(final Bill bill) {
        final Cell pdfPCell = new Cell();
        final String manDays = String.format("%,.1f", bill.getBillConfig().getManDays());
        final String format = "%6s";
        final String formatted = String.format(format, manDays);
        final Paragraph paragraph = this.utils.getSmall(formatted);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        pdfPCell.add(paragraph);
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private Cell addPuHt(final Bill bill) {
        final Cell pdfPCell = new Cell();
        final String dailyFee = String.format("%,.2f", bill.getBillConfig().getJob().getDailyFee());
        final String format = "%8s";
        final String formatted = String.format(format, dailyFee);
        final Paragraph paragraph = this.utils.getSmall(formatted);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        pdfPCell.add(paragraph);
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private Cell addPrestation(final Bill bill) {
        final Cell pdfPCell = new Cell();
        pdfPCell.add(this.utils.getSmall(bill.getBillConfig().getJob().getTitle()));
        final String periodLabel = this.labelsConfig.getDetailLabels().getJobLabels().getPeriodLabels().getLabel();
        final String periodMessage = periodLabel + this.getPeriod(bill);
        final Paragraph paragraph = this.utils.getSmall(periodMessage);
        paragraph.setMarginBottom(20);
        pdfPCell.add(paragraph);
        final String description = bill.getBillConfig().getJob().getDescription();
        final String jobContent = this.labelsConfig.getDetailLabels().getJobLabels().getContent() + description;
        pdfPCell.add(this.utils.getSmall(jobContent));
        //
        final String start = bill.getBillConfig().getJob().getStart();
        final String startLabel = this.labelsConfig.getDetailLabels().getJobLabels().getStart();
        final String jobStart = startLabel + start;
        pdfPCell.add(this.utils.getSmall(jobStart));
        //
        final String end = bill.getBillConfig().getJob().getEnd();
        final String endLabel = this.labelsConfig.getDetailLabels().getJobLabels().getEnd();
        final String jobEnd = endLabel + end;
        pdfPCell.add(this.utils.getSmall(jobEnd));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private Cell addRef(final Bill bill) {
        final Cell pdfPCell = new Cell();
        pdfPCell.add(this.utils.getSmall(bill.getReference()));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private String getPeriod(final Bill bill) {
        final int month = bill.getBillConfig().getMonth();
        final int year = bill.getBillConfig().getYear();
        final YearMonth yearMonth = YearMonth.of(year, month);
        final Date convertedFromYearMonth =
                Date.from(yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final String pattern = this.labelsConfig.getDetailLabels().getJobLabels().getPeriodLabels().getPattern();
        final String locale = this.labelsConfig.getDetailLabels().getJobLabels().getPeriodLabels().getLocale();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale(locale));
        return simpleDateFormat.format(convertedFromYearMonth);
    }
}
