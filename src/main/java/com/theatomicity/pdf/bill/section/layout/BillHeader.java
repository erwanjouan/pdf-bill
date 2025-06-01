package com.theatomicity.pdf.bill.section.layout;

import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.section.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.theatomicity.pdf.bill.model.Constants.THEME_COLOR;

@Component
public class BillHeader implements Section {

    @Autowired
    private PdfPage page;

    @Override
    public void render(final Bill bill) {
        final PdfCanvas canvas = new PdfCanvas(this.page);
        canvas.setFillColor(THEME_COLOR);
        canvas.rectangle(0, 815, 800, 845);
        canvas.fill();
    }
}
