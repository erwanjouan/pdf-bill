package com.theatomicity.pdf.bill.section.layout;


import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.theatomicity.pdf.bill.model.Constants.THEME_COLOR;

@Component
public class BillFooter implements Section {

    @Autowired
    private PdfPage page;

    public void render(final Bill bill) {
        final PdfCanvas canvas = new PdfCanvas(this.page);
        canvas.setFillColor(THEME_COLOR);
        canvas.rectangle(0, 0, 800, 30);
        canvas.fill();
    }
}
