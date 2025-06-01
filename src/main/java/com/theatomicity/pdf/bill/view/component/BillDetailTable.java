package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Config;
import com.theatomicity.pdf.bill.model.Template;

import static com.theatomicity.pdf.bill.model.Constants.HEADER_COLOR;
import static com.theatomicity.pdf.bill.model.Constants.SMALL;
import static com.theatomicity.pdf.bill.view.component.CommonBill.getFormattedAmount;

public class BillDetailTable {

    private final Config config;

    public BillDetailTable(final Config config) {
        this.config = config;
    }

    public PdfPTable get(final Bill bill) throws DocumentException {
        final PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{1, 4, 1, 1, 2});
        this.addTableHeader(table);
        this.addRows(table, bill);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        return table;
    }

    private void addTableHeader(final PdfPTable table) {
        addHeaderCell(table, "Ref.");
        addHeaderCell(table, "Désignation de la prestation");
        addHeaderCell(table, "PU HT €");
        addHeaderCell(table, "Quantité");
        addHeaderCell(table, "Montant HT €");
    }

    private static void addHeaderCell(final PdfPTable table, final String label) {
        final PdfPCell header = new PdfPCell();
        header.setBackgroundColor(HEADER_COLOR);
        header.addElement(new Paragraph(label, SMALL));
        header.setPadding(5);
        table.addCell(header);
    }

    private void addRows(final PdfPTable table, final Bill bill) {
        table.addCell(this.addRef(bill));
        table.addCell(this.addPrestation(bill));
        table.addCell(this.addPuHt());
        table.addCell(this.addQuantite(bill));
        table.addCell(this.addMontantHt(bill));
    }

    private PdfPCell addMontantHt(final Bill bill) {
        final PdfPCell pdfPCell = new PdfPCell();
        final String formattedAmount = getFormattedAmount(bill.getMonthlyNetFee());
        pdfPCell.addElement(new Paragraph(formattedAmount, SMALL));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private PdfPCell addQuantite(final Bill bill) {
        final PdfPCell pdfPCell = new PdfPCell();
        final String manDays = String.format("%,.1f", bill.getManDays());
        final String format = "%6s";
        final String formatted = String.format(format, manDays);
        pdfPCell.addElement(new Paragraph(formatted, SMALL));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private PdfPCell addPuHt() {
        final PdfPCell pdfPCell = new PdfPCell();
        final String dailyFee = String.format("%,.2f", this.config.getDailyFee());
        final String format = "%8s";
        final String formatted = String.format(format, dailyFee);
        final Paragraph paragraph = new Paragraph(formatted, SMALL);
        pdfPCell.addElement(paragraph);
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private PdfPCell addPrestation(final Bill bill) {
        final PdfPCell pdfPCell = new PdfPCell();
        final Template template = this.config.getTemplate();
        pdfPCell.addElement(new Paragraph(template.getJobTitle(), SMALL));
        final String periodMessage = String.format("Période: %s", bill.getPeriodFr());
        final Paragraph paragraph = new Paragraph(periodMessage, SMALL);
        paragraph.setSpacingAfter(20);
        pdfPCell.addElement(paragraph);
        final String jobContent = String.format("Prestation: %s", template.getJobContent());
        pdfPCell.addElement(new Paragraph(jobContent, SMALL));
        final String jobStart = String.format("Date de début : %s", this.config.getJobStart());
        pdfPCell.addElement(new Paragraph(jobStart, SMALL));
        final String jobEnd = String.format("Date de fin : %s", this.config.getJobEnd());
        pdfPCell.addElement(new Paragraph(jobEnd, SMALL));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }

    private PdfPCell addRef(final Bill bill) {
        final PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.addElement(new Paragraph(bill.getReference(), SMALL));
        pdfPCell.setPadding(5);
        return pdfPCell;
    }
}
