package com.theatomicity.pdf.bill.section.upper;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.theatomicity.pdf.bill.config.LabelsConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import com.theatomicity.pdf.bill.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillDate implements Section {

    @Autowired
    private Document document;

    @Autowired
    private Utils utils;

    @Autowired
    private LabelsConfig labelsConfig;

    @Override
    public void render(final Bill bill) {
        final String dateLabel = this.labelsConfig.getDate();
        final String date = this.utils.getFormattedDate(bill.getDate());
        final Paragraph paragraph = this.utils.getMedium(dateLabel + date);
        paragraph.setTextAlignment(TextAlignment.RIGHT);
        paragraph.setMarginTop(30);
        paragraph.setMarginBottom(40);
        this.document.add(paragraph);
    }
}
