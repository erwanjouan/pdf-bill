package com.theatomicity.pdf.bill.model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import java.time.format.DateTimeFormatter;

public class Constants {

    private Constants() {
    }

    public static final String CONFIG_YML = "config.yml";

    public static final Font VERY_SMALL = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, BaseColor.BLACK);
    public static final Font SMALL = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
    public static final Font SMALL_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
    //
    public static final BaseColor HEADER_COLOR = new BaseColor(220, 220, 220);
    public static final BaseColor ATOMICITY_COLOR = new BaseColor(33, 136, 127);
    //
    public static final String OUTPUT_FOLDER = "output";
    public static final String FILE_PATTERN = "Facture_%s_%s_%04d%02d.pdf";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
