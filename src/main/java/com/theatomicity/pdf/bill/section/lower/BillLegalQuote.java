package com.theatomicity.pdf.bill.section.lower;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import com.theatomicity.pdf.bill.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillLegalQuote implements Section {

    @Autowired
    private Document document;

    @Autowired
    private Utils utils;

    @Override
    public void render(final Bill bill) {
        final String disclaimer = bill.getBillConfig().getDisclaimer();
        final Paragraph paragraph = this.utils.getVerySmall(disclaimer);
        paragraph.setMarginTop(20);
        paragraph.setMarginBottom(20);
        this.document.add(paragraph);
    }
}
