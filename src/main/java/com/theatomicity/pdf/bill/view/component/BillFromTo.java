package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.theatomicity.pdf.bill.model.Company;
import com.theatomicity.pdf.bill.model.Config;

import static com.theatomicity.pdf.bill.model.Constants.SMALL;
import static com.theatomicity.pdf.bill.model.Constants.SMALL_BOLD;

public class BillFromTo {

    private final Config config;

    public BillFromTo(final Config config) {
        this.config = config;
    }

    public PdfPTable get() throws DocumentException {
        final PdfPTable table = new PdfPTable(4);
        table.setWidths(new float[]{2, 10, 2, 10});
        table.addCell(this.getFromHeader());
        table.addCell(this.getFrom());
        table.addCell(this.getToHeader());
        table.addCell(this.getTo());
        table.setSpacingAfter(20);
        table.setWidthPercentage(100);
        return table;
    }

    private PdfPCell getToHeader() {
        return this.fromToKeyWord("A :");
    }

    private PdfPCell getFromHeader() {
        return this.fromToKeyWord("De :");
    }

    private PdfPCell fromToKeyWord(final String word) {
        final Font underline = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
        final PdfPCell pdfPCell = new PdfPCell();
        final Paragraph paragraph = new Paragraph(word, underline);
        pdfPCell.addElement(paragraph);
        pdfPCell.setPadding(5);
        pdfPCell.setBorderWidth(0);
        return pdfPCell;
    }

    PdfPCell getFrom() {
        final PdfPCell pdfPCell = new PdfPCell();
        final Company fromCompany = this.config.getTemplate().getFromCompany();
        //
        pdfPCell.addElement(new Paragraph(fromCompany.getName(), SMALL_BOLD));
        //
        pdfPCell.addElement(new Paragraph(fromCompany.getStatus(), SMALL));
        //
        for (final String addressLine : fromCompany.getAddress()) {
            pdfPCell.addElement(new Paragraph(addressLine, SMALL));
        }
        //
        pdfPCell.addElement(new Paragraph(fromCompany.getPhone(), SMALL));
        //
        final Anchor anchor = CommonBill.getAnchor(fromCompany.getEmail(), String.format("mailto:%s", fromCompany.getEmail()));
        pdfPCell.addElement(new Paragraph(anchor));
        //
        for (final String legalLine : fromCompany.getLegal()) {
            pdfPCell.addElement(new Paragraph(legalLine, SMALL));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorderWidth(0);
        return pdfPCell;
    }

    PdfPCell getTo() {
        final PdfPCell pdfPCell = new PdfPCell();
        final Company toCompany = this.config.getTemplate().getToCompany();
        //
        pdfPCell.addElement(new Paragraph(toCompany.getName(), SMALL_BOLD));
        //
        for (final String address : toCompany.getAddress()) {
            pdfPCell.addElement(new Paragraph(address, SMALL));
        }
        //
        final String website = toCompany.getWebsite();
        final Anchor anchor = CommonBill.getAnchor(website, website);
        pdfPCell.addElement(new Paragraph(anchor));
        //
        for (final String legal : toCompany.getLegal()) {
            pdfPCell.addElement(new Paragraph(legal, SMALL));
        }
        pdfPCell.setPadding(5);
        pdfPCell.setBorderWidth(0);
        return pdfPCell;
    }

}
