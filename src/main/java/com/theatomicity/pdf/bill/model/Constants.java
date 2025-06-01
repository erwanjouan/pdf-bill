package com.theatomicity.pdf.bill.model;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;

public class Constants {

    private Constants() {
    }

    public static final String LABELS_CONFIG_YML = "labels.config.fr.yml";
    public static final String DEFAULT_INPUT_CONFIG_JSON = "input/billing.config.json";
    public static final DeviceRgb THEME_COLOR = new DeviceRgb(33, 136, 127);
    public static final Color HEADER_COLOR = new DeviceRgb(220, 220, 220);
}
