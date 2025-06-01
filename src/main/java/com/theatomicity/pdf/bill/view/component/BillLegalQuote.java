package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.Paragraph;
import com.theatomicity.pdf.bill.model.Config;

import static com.theatomicity.pdf.bill.model.Constants.VERY_SMALL;

public class BillLegalQuote {

    private final Config config;

    public BillLegalQuote(final Config config) {
        this.config = config;
    }

    public Paragraph get() {
        final String disclaimer = this.config.getTemplate().getDisclaimer();
        final Paragraph paragraph = new Paragraph(disclaimer, VERY_SMALL);
        paragraph.setSpacingBefore(20);
        paragraph.setSpacingAfter(20);
        return paragraph;
    }
}
