package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.Rectangle;

import static com.theatomicity.pdf.bill.model.Constants.ATOMICITY_COLOR;

public class BillHeader {

    public Rectangle get() {
        final Rectangle header = new Rectangle(0, 815, 800, 845);
        header.setBackgroundColor(ATOMICITY_COLOR);
        return header;
    }
}
