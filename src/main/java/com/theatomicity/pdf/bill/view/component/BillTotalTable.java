package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Config;

import java.math.BigDecimal;

import static com.theatomicity.pdf.bill.model.Constants.SMALL;
import static com.theatomicity.pdf.bill.view.component.CommonBill.getFormattedAmount;

public class BillTotalTable {

    private final Config config;

    public BillTotalTable(final Config config) {
        this.config = config;
    }

    public PdfPTable get(final Bill bill) throws DocumentException {
        final PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(20);
        table.setWidths(new float[]{5, 4});
        table.setWidthPercentage(100);
        this.addRows(table, bill);
        return table;
    }

    private void addRows(final PdfPTable table, final Bill bill) {
        table.addCell(this.addCondition());
        table.addCell(this.addTotals(bill));
    }

    public PdfPCell addTotals(final Bill bill) {
        final PdfPCell pdfPCell = new PdfPCell();
        final PdfPTable subTable = new PdfPTable(2);
        this.addLine(subTable, "Total HT €", bill.getMonthlyNetFee());
        final String vatLabel = String.format("TVA (%,.0f %%) €", this.config.getTemplate().getVat());
        this.addLine(subTable, vatLabel, bill.getVatFee());
        this.addLine(subTable, "Total TTC €", bill.getMonthlyGrossFee());
        pdfPCell.addElement(subTable);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        return pdfPCell;
    }

    private void addLine(final PdfPTable subTable, final String label, final BigDecimal bigDecimal) {
        final PdfPCell pdfPCellLabel = new PdfPCell();
        final Paragraph paragraph = new Paragraph(label, SMALL);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        pdfPCellLabel.addElement(paragraph);
        pdfPCellLabel.setPaddingBottom(5);
        pdfPCellLabel.setPaddingRight(5);
        subTable.addCell(pdfPCellLabel);
        final PdfPCell pdfPCellValue = new PdfPCell();
        final String formattedAmount = getFormattedAmount(bigDecimal);
        pdfPCellValue.addElement(new Paragraph(formattedAmount, SMALL));
        subTable.addCell(pdfPCellValue);
    }

    private PdfPCell addCondition() {
        final PdfPCell pdfPCell = new PdfPCell();
        for (final String bankInfo : this.config.getTemplate().getBankInfo()) {
            pdfPCell.addElement(new Paragraph(bankInfo, SMALL));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        return pdfPCell;
    }
}
