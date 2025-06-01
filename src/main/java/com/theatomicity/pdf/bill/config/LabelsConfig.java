package com.theatomicity.pdf.bill.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LabelsConfig {
    private String referenceLabel;
    private String referencePattern;
    private String date;
    private String from;
    private String to;
    private String emailUrl;
    private DetailLabels detailLabels;
    private TotalLabels totalLabels;
    private Output output;

    @Data
    @NoArgsConstructor
    public static class DetailLabels {
        private String ref;
        private String content;
        private String quantity;
        private String unitPrice;
        private JobLabels jobLabels;
    }

    @Data
    @NoArgsConstructor
    public static class JobLabels {
        private String start;
        private String end;
        private String content;
        private PeriodLabels periodLabels;
    }

    @Data
    @NoArgsConstructor
    public static class PeriodLabels {
        private String label;
        private String pattern;
        private String locale;
    }

    @Data
    @NoArgsConstructor
    public static class TotalLabels {
        private String net;
        private String vatPattern;
        private String gross;
    }

    @Data
    @NoArgsConstructor
    public static class Output {
        private String folder;
        private String tmpFile;
        private String pattern;
    }
}