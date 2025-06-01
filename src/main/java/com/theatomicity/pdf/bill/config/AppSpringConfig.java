package com.theatomicity.pdf.bill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.theatomicity.pdf.bill.model.Constants.LABELS_CONFIG_YML;

@Configuration
@ComponentScan(basePackages = "com.theatomicity.pdf.bill")
public class AppSpringConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Profile("awsLambda")
    public Path outputPathAws() {
        return Paths.get("/tmp");
    }

    @Bean
    @Profile("!awsLambda")
    public Path outputPathLocal() {
        return Paths.get("output");
    }

    @Bean
    public File outputFile(final Path rootPath) {
        return new File(rootPath.toString(), "output.pdf");
    }

    @Bean
    public PdfDocument pdfDocument(final File outputFile) throws IOException {
        final PdfWriter writer = new PdfWriter(outputFile.getAbsolutePath());
        return new PdfDocument(writer);
    }

    @Bean
    public Document document(final PdfDocument pdf) {
        pdf.setDefaultPageSize(PageSize.A4);
        return new Document(pdf);
    }

    @Bean
    public PdfPage pdfPage(final PdfDocument pdf) {
        return pdf.addNewPage();
    }

    @Bean
    public LabelsConfig labelsConfig() {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            final URI uri = Objects.requireNonNull(this.getClass().getClassLoader().getResource(LABELS_CONFIG_YML)).toURI();
            final String yamlSource = Files.readString(Paths.get(uri));
            return mapper.readValue(yamlSource, LabelsConfig.class);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
