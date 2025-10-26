package com.theatomicity.pdf.bill;

import com.theatomicity.pdf.bill.config.AppSpringConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.presenter.Controller;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;

import static com.theatomicity.pdf.bill.model.Constants.DEFAULT_INPUT_CONFIG_JSON;

public class LocalApp {

    public static void main(final String[] args) throws IOException {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppSpringConfig.class);
        final Controller controller = context.getBean(Controller.class);
        // custom input can be passed as program arg
        final String inputJson = (args.length == 1) ? args[0] : DEFAULT_INPUT_CONFIG_JSON;
        final Bill bill = controller.getBillFromJsonFile(inputJson);
        controller.render(bill);
        final File pdf = controller.getFile(bill);
        Runtime.getRuntime().exec("open " + pdf);

    }
}
