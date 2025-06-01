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

import java.math.BigDecimal;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.theatomicity.pdf.bill.model.Constants.HEADER_COLOR;

@Component
public class BillTotalTable implements Section {

    @Autowired
    private Document document;

    @Autowired
    private Utils utils;

    @Autowired
    private LabelsConfig labelsConfig;

    public void render(final Bill bill) {
        final float[] columnWidth = {380, 200};
        final Table table = new Table(columnWidth);
        table.setMarginTop(30);
        table.setWidth(UnitValue.createPercentValue(100));
        this.addRows(table, bill);
        this.document.add(table);
    }

    private void addRows(final Table table, final Bill bill) {
        table.addCell(this.addCondition(bill));
        table.addCell(this.addTotals(bill));
    }

    private Cell addTotals(final Bill bill) {
        final Cell pdfPCell = new Cell();
        final float[] colWidth = {270, 200};
        final Table subTable = new Table(colWidth);

        final String netLabel = this.labelsConfig.getTotalLabels().getNet();
        this.addLine(subTable, netLabel, bill.getMonthlyNetFee());

        final String vatPattern = this.labelsConfig.getTotalLabels().getVatPattern();
        final String vatLabel = String.format(vatPattern, bill.getBillConfig().getJob().getVat());
        this.addLine(subTable, vatLabel, bill.getVatFee());

        final String grossLabel = this.labelsConfig.getTotalLabels().getGross();
        this.addLine(subTable, grossLabel, bill.getMonthlyGrossFee());

        subTable.setWidth(UnitValue.createPercentValue(100));
        pdfPCell.add(subTable);
        pdfPCell.setBorder(NO_BORDER);
        return pdfPCell;
    }

    private void addLine(final Table subTable, final String label, final BigDecimal bigDecimal) {
        final Cell pdfPCellLabel = new Cell();
        final Paragraph paragraph = this.utils.getSmall(label);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        pdfPCellLabel.add(paragraph);
        pdfPCellLabel.setPaddingRight(5);
        pdfPCellLabel.setBackgroundColor(HEADER_COLOR);
        subTable.addCell(pdfPCellLabel);
        final Cell pdfPCellValue = new Cell();
        final String formattedAmount = this.utils.getFormattedAmount(bigDecimal);
        final Paragraph amount = this.utils.getSmall(formattedAmount);
        amount.setTextAlignment(TextAlignment.RIGHT);
        pdfPCellValue.add(amount);
        pdfPCellValue.setPaddingLeft(5);
        subTable.addCell(pdfPCellValue);
    }


    private Cell addCondition(final Bill bill) {
        final Cell pdfPCell = new Cell();
        for (final String bankInfo : bill.getBillConfig().getBankInfo()) {
            pdfPCell.add(this.utils.getSmall(bankInfo));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorder(NO_BORDER);
        return pdfPCell;
    }
}
