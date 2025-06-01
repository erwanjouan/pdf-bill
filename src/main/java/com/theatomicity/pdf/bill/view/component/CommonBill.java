package com.theatomicity.pdf.bill.view.component;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CommonBill {
    private CommonBill() {
    }

    static String getFormat(final String input) {
        final int length = 21 - input.length();
        return "%" + length + "s";
    }

    static String getFormattedAmount(final BigDecimal bigDecimal) {
        final DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false);
        df.setMinimumFractionDigits(2);
        final String result = df.format(bigDecimal);
        final String format = getFormat(result);
        return String.format(format, result);
    }

    static Anchor getAnchor(final String text, final String url) {
        final Font anchorFont = new Font(Font.FontFamily.UNDEFINED, 10);
        anchorFont.setColor(BaseColor.BLUE);
        anchorFont.setStyle(Font.FontStyle.UNDERLINE.getValue());
        final Anchor anchor = new Anchor(text, anchorFont);
        anchor.setReference(url);
        return anchor;
    }
}
