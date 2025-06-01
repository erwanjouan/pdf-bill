package com.theatomicity.pdf.bill.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.theatomicity.pdf.bill.model.Bill;

import java.io.FileNotFoundException;

public interface View {

    void generate(Bill bill, Document document) throws FileNotFoundException, DocumentException;

}
