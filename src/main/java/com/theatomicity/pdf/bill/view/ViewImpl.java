package com.theatomicity.pdf.bill.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.model.Config;
import com.theatomicity.pdf.bill.view.component.BillDate;
import com.theatomicity.pdf.bill.view.component.BillDetailTable;
import com.theatomicity.pdf.bill.view.component.BillFooter;
import com.theatomicity.pdf.bill.view.component.BillFromTo;
import com.theatomicity.pdf.bill.view.component.BillHeader;
import com.theatomicity.pdf.bill.view.component.BillLegalQuote;
import com.theatomicity.pdf.bill.view.component.BillReference;
import com.theatomicity.pdf.bill.view.component.BillTotalTable;

import java.io.FileNotFoundException;

public class ViewImpl implements View {

    private final BillHeader billHeader;
    private final BillReference billReference;
    private final BillDate billDate;
    private final BillFromTo billFromTo;
    private final BillDetailTable billDetailTable;
    private final BillTotalTable billTotalTable;
    private final BillLegalQuote billLegalQuote;
    private final BillFooter billFooter;
    private final Config config;

    public ViewImpl(final Config config) {
        this.config = config;
        this.billHeader = new BillHeader();
        this.billReference = new BillReference(config);
        this.billDate = new BillDate();
        this.billFromTo = new BillFromTo(config);
        this.billDetailTable = new BillDetailTable(config);
        this.billTotalTable = new BillTotalTable(config);
        this.billLegalQuote = new BillLegalQuote(config);
        this.billFooter = new BillFooter();
    }

    @Override
    public void generate(final Bill bill, final Document document) throws FileNotFoundException, DocumentException {
        document.open();
        document.add(this.billHeader.get());
        document.add(this.billReference.get(bill));
        document.add(this.billDate.get(bill));
        document.add(this.billFromTo.get());
        document.add(this.billDetailTable.get(bill));
        document.add(this.billTotalTable.get(bill));
        document.add(this.billLegalQuote.get());
        document.add(this.billFooter.get());
        document.close();
    }
}
