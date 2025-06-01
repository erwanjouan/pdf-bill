package com.theatomicity.pdf.bill;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.theatomicity.pdf.bill.config.AppSpringConfig;
import com.theatomicity.pdf.bill.model.Bill;
import com.theatomicity.pdf.bill.presenter.Controller;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public static final String TMP = "/tmp";
    private S3AsyncClient s3AsyncClient;

    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        final String bucket = System.getenv("PDF_BILL_BUCKET");
        final String inputJson = input.getBody();
        final AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppSpringConfig.class);
        final Controller controller = appContext.getBean(Controller.class);
        final Bill bill = controller.getBillFromJson(inputJson);
        controller.render(bill);
        final File pdfFile = controller.getFile(bill);
        this.uploadToS3(bucket, pdfFile);
        final String presignedURL = this.getPresignedURL(bucket, pdfFile);
        final Map<String, String> headers = this.getHeaders(presignedURL);
        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers)
                .withStatusCode(302);
    }

    private void uploadToS3(final String bucket, final File pdfFile) {
        this.getS3AsyncClient().putObject(
                req -> req.bucket(bucket).key(pdfFile.getName()),
                AsyncRequestBody.fromFile(Paths.get(TMP, pdfFile.getName()))
        ).join();
    }

    private Map<String, String> getHeaders(final String presignedURL) {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Location", presignedURL);
        return headers;
    }

    private String getPresignedURL(final String bucket, final File pdfFile) {
        final S3Presigner presigner = S3Presigner.builder().build();
        final GetObjectPresignRequest presignRequest = GetObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(pdfFile.getName()).build())
                .build();
        final PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    private S3AsyncClient getS3AsyncClient() {
        if (this.s3AsyncClient == null) {
            this.s3AsyncClient = S3AsyncClient.crtBuilder()
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .region(Region.EU_WEST_1)
                    .targetThroughputInGbps(20.0)
                    .minimumPartSizeInBytes(8 * 1025 * 1024L)
                    .build();
        }
        return this.s3AsyncClient;
    }

}
