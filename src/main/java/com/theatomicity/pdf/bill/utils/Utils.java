package com.theatomicity.pdf.bill.utils;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD;
import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA_OBLIQUE;
import static com.itextpdf.kernel.colors.DeviceRgb.BLACK;
import static com.theatomicity.pdf.bill.model.Constants.THEME_COLOR;

@Component
public class Utils {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Paragraph getBigBold(final String text) {
        return this.getStyledParagraph(text, HELVETICA_BOLD, 16, BLACK);
    }

    public Paragraph getMedium(final String text) {
        return this.getStyledParagraph(text, HELVETICA, 12, BLACK);
    }

    public Paragraph getSmallBold(final String text) {
        return this.getStyledParagraph(text, HELVETICA_BOLD, 11, BLACK);
    }

    public Paragraph getSmall(final String text) {
        return this.getStyledParagraph(text, HELVETICA, 10, BLACK);
    }

    public Paragraph getVerySmall(final String text) {
        return this.getStyledParagraph(text, HELVETICA_OBLIQUE, 9, BLACK);
    }

    public Paragraph getSmallHyperlink(final String text, final String uri) {
        final PdfAction action = PdfAction.createURI(uri);
        final Link link = new Link(text, action);
        final Text styledText = this.getStyledText(link, HELVETICA, 10, THEME_COLOR);
        return new Paragraph().add(styledText);
    }

    public Text getStyledString(final String text, final String fontProgram, final int fontSize, final Color color) {
        return this.getStyledText(new Text(text), fontProgram, fontSize, color);
    }

    public Text getStyledText(final Text text, final String fontProgram, final int fontSize, final Color color) {
        try {
            final PdfFont font = PdfFontFactory.createFont(fontProgram);
            return text.setFont(font).setFontSize(fontSize).setFontColor(color);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Paragraph getStyledParagraph(final String text, final String fontProgram, final int fontSize, final Color color) {
        final Text styledText = this.getStyledString(text, fontProgram, fontSize, color);
        return new Paragraph().add(styledText);
    }

    public String getFormattedAmount(final BigDecimal bigDecimal) {
        final DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.applyPattern("##,##0.00");
        return df.format(bigDecimal);
    }

    public String getFormattedDate(final LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

}

