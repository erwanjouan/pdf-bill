package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Config;

public class BillReference {

    private final Config config;

    public BillReference(final Config config) {
        this.config = config;
    }

    public Paragraph get(final Bill bill) {
        final Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        final String referenceLabel = this.config.getTemplate().getReference();
        final String reference = String.format(referenceLabel, bill.getFullReference());
        final Paragraph paragraph = new Paragraph(reference, font);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(50);
        return paragraph;
    }


}
