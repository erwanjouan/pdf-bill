package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.Rectangle;

import static com.theatomicity.pdf.bill.model.Constants.ATOMICITY_COLOR;

public class BillFooter {

    public Rectangle get() {
        final Rectangle header = new Rectangle(800, 30);
        header.setBackgroundColor(ATOMICITY_COLOR);

        return header;
    }
}
