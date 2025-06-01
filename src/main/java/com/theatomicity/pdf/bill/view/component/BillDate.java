package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.theatomicity.pdf.bill.model.Bill;

import java.time.LocalDate;

import static com.theatomicity.pdf.bill.model.Constants.DATE_FORMATTER;

public class BillDate {

    public Paragraph get(final Bill bill) {
        final Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        final String date = this.getFormattedDate(bill);
        final String dateMessage = String.format("Date : %s", date);
        final Paragraph paragraph = new Paragraph(dateMessage, font);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(30);
        paragraph.setSpacingAfter(40);
        paragraph.setIndentationLeft(50);
        return paragraph;
    }

    private String getFormattedDate(final Bill bill) {
        final LocalDate date = bill.getDate();
        return date.format(DATE_FORMATTER);
    }
}
