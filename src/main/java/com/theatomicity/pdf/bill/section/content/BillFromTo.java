package com.theatomicity.pdf.bill.section.content;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.theatomicity.pdf.bill.config.LabelsConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Company;
import com.theatomicity.pdf.bill.section.Section;
import com.theatomicity.pdf.bill.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.itextpdf.layout.borders.Border.NO_BORDER;

@Component
public class BillFromTo implements Section {

    @Autowired
    private Document document;

    @Autowired
    private Utils utils;

    @Autowired
    private LabelsConfig labelsConfig;

    @Override
    public void render(final Bill bill) {
        final float[] columnWidth = {200, 1000, 200, 1000};
        final Table table = new Table(columnWidth);

        final String fromLabel = this.labelsConfig.getFrom();
        table.addCell(this.fromToKeyWord(fromLabel));
        table.addCell(this.getFrom(bill));

        final String toLabel = this.labelsConfig.getTo();
        table.addCell(this.fromToKeyWord(toLabel));
        table.addCell(this.getTo(bill));

        this.document.add(table);
    }

    private Cell fromToKeyWord(final String word) {
        final Paragraph paragraph = this.utils.getSmallBold(word);
        final Cell cell = new Cell().add(paragraph);
        cell.setPadding(5);
        cell.setBorder(NO_BORDER);
        return cell;
    }

    private Cell getFrom(final Bill bill) {
        final Cell pdfPCell = new Cell();
        final Company fromCompany = bill.getBillConfig().getFrom();
        //
        pdfPCell.add(this.utils.getSmallBold(fromCompany.getName()));
        pdfPCell.add(this.utils.getSmall(fromCompany.getStatus()));
        //
        for (final String addressLine : fromCompany.getAddress()) {
            pdfPCell.add(this.utils.getSmall(addressLine));
        }
        //
        pdfPCell.add(this.utils.getSmall(fromCompany.getPhone()));
        //
        final String emailUrl = this.labelsConfig.getEmailUrl();
        final String uri = String.format(emailUrl, fromCompany.getEmail());
        pdfPCell.add(this.utils.getSmallHyperlink(fromCompany.getEmail(), uri));
        //
        for (final String legalLine : fromCompany.getLegal()) {
            pdfPCell.add(this.utils.getSmall(legalLine));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorder(NO_BORDER);
        return pdfPCell;
    }

    Cell getTo(final Bill bill) {
        final Cell pdfPCell = new Cell();
        final Company toCompany = bill.getBillConfig().getTo();
        pdfPCell.add(this.utils.getSmallBold(toCompany.getName()));
        for (final String address : toCompany.getAddress()) {
            pdfPCell.add(this.utils.getSmall(address));
        }
        final String website = toCompany.getWebsite();
        final Paragraph smallHyperlink = this.utils.getSmallHyperlink(website, website);
        pdfPCell.add(smallHyperlink);
        //
        for (final String legal : toCompany.getLegal()) {
            pdfPCell.add(this.utils.getSmall(legal));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorder(NO_BORDER);
        return pdfPCell;
    }
}
