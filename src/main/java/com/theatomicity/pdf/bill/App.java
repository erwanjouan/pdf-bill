package com.theatomicity.pdf.bill;

import com.itextpdf.text.DocumentException;
import com.theatomicity.pdf.bill.presenter.Presenter;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main(final String[] args) throws IOException, DocumentException {
        String configFileName = args.length == 1 ? args[0] : "config.yml";
        final File pdf = new Presenter(configFileName).generate();
        Runtime.getRuntime().exec("open " + pdf);
    }
}
