package com.theatomicity.pdf.bill.presenter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AwsPresenter extends Presenter {

    private final String monthStr;
    private final String yearStr;
    private final String manDaysStr;

    public AwsPresenter(final String configFile, final String monthStr, final String yearStr, final String manDaysStr) {
        super(configFile);
        this.monthStr = monthStr;
        this.yearStr = yearStr;
        this.manDaysStr = manDaysStr;
    }

    @Override
    protected String getOutputFolder() {
        return "/tmp";
    }

    @Override
    protected int getYear() {
        return Integer.valueOf(this.yearStr);
    }

    @Override
    protected int getMonth() {
        return Integer.valueOf(this.monthStr);
    }

    @Override
    protected BigDecimal getManDays() {
        return new BigDecimal(this.manDaysStr).setScale(2, RoundingMode.HALF_UP);
    }
}
