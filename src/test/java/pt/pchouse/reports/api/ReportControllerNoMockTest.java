package pt.pchouse.reports.api;

import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import pt.pchouse.reports.api.generator.GeneratorTest;
import pt.pchouse.reports.api.request.Metadata;
import pt.pchouse.reports.api.request.Parameter;
import pt.pchouse.reports.api.request.PdfProperties;
import pt.pchouse.reports.api.request.ReportRequest;
import pt.pchouse.reports.api.request.datasource.Database;
import pt.pchouse.reports.api.response.ReportResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportControllerNoMockTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportController reportController;

    @Test
    void contextLoads() {
        assertThat(reportController).isNotNull();
    }

    @Autowired
    ApplicationContext applicationContext;

    final static String title = "The title";
    final static String author = "The author";
    final static String subject = "The subject";
    final static String keywords = "The keywords";
    final static String application = "The application";
    final static String creator = "The creator";

    @BeforeAll
    public static void beforeAll() throws URISyntaxException, IOException {
        Path exportDir = Paths.get(
                Paths.get(
                        Objects.requireNonNull(
                                GeneratorTest.class.getClassLoader().getResource("sakila")
                        ).toURI()).getParent().toFile().getAbsolutePath(),
                "generated_api_reports"
        );

        if (Files.exists(exportDir)) {
            for (File file : Objects.requireNonNull(exportDir.toFile().listFiles())) {
                if (file.isFile()) {
                    Files.delete(Paths.get(file.getAbsolutePath()));
                }
            }
        } else {
            Files.createDirectory(exportDir);
        }
    }


    public ReportRequest getReportRequestForTests() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties dbProp = new Properties();
        dbProp.load(classLoader.getResourceAsStream("sakila/mysql.properties"));

        Database database = applicationContext.getBean(
                Database.class,
                dbProp.getProperty("connection"),
                dbProp.getProperty("driver"),
                dbProp.getProperty("user"),
                dbProp.getProperty("password")
        );

        URL jasperPath = classLoader.getResource("sakila/sakila.jasper");
        if (jasperPath == null) {
            throw new Exception("sakila.jasper not load");
        }

        String base64Jasper = Base64.getEncoder().encodeToString(
                Files.readAllBytes(
                        Paths.get(jasperPath.toURI())
                )
        );

        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(Parameter.Types.P_STRING, "P_STRING", "The string parameter"));
        parameters.add(new Parameter(Parameter.Types.P_BOOLEAN, "P_BOOLEAN", "true"));
        parameters.add(new Parameter(Parameter.Types.P_DOUBLE, "P_DOUBLE", "9.99"));
        parameters.add(new Parameter(Parameter.Types.P_FLOAT, "P_FLOAT", "0.99"));
        parameters.add(new Parameter(Parameter.Types.P_INTEGER, "P_INTEGER", "999"));
        parameters.add(new Parameter(Parameter.Types.P_LONG, "P_LONG", "9999"));
        parameters.add(new Parameter(Parameter.Types.P_SHORT, "P_SHORT", "9"));
        parameters.add(new Parameter(Parameter.Types.P_BIGDECIMAL, "P_BIG_DECIMAL", "99999.99"));
        parameters.add(new Parameter(Parameter.Types.P_SQL_DATE, "P_SQL_DATE", "1969-10-05"));
        parameters.add(new Parameter(Parameter.Types.P_SQL_TIME, "P_SQL_TIME", "09:12:29"));
        parameters.add(new Parameter(Parameter.Types.P_TIMESTAMP, "P_TIMESTAMPT", "1969-10-05 09:12:29"));
        parameters.add(new Parameter(Parameter.Types.P_DATE, "P_DATE", "1969-10-05", "yyyy-MM-dd"));
        parameters.add(new Parameter(Parameter.Types.P_INTEGER, "CHARACTER_WIDTH", "1"));

        Metadata metadata = new Metadata();

        metadata.setTitle(title);
        metadata.setAuthor(author);
        metadata.setSubject(subject);
        metadata.setKeywords(keywords);
        metadata.setApplication(application);
        metadata.setCreator(creator);

        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setParameters(parameters);
        reportRequest.setReport(base64Jasper);
        reportRequest.setDatabase(database);
        reportRequest.setMetadata(metadata);

        return reportRequest;
    }


    @Test
    void testGetReport() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        String url = "http://localhost:" + port + "/report";

        for (ReportRequest.ReportType reportType : ReportRequest.ReportType.values()) {

            if (reportType.equals(ReportRequest.ReportType.PRINT)) {
                continue;
            }

            ReportRequest reportRequest = getReportRequestForTests();
            reportRequest.setReportType(reportType);

            ReportResponse reportResponse = restTemplate.postForObject(
                    url,
                    reportRequest,
                    ReportResponse.class
            );

            assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
            assertThat(reportResponse.getReport()).isNotEmpty();
            assertThat(reportResponse.getMessage()).isEqualTo("");

            Path pdfPath = Paths.get(
                    Paths.get(
                            Objects.requireNonNull(
                                    classLoader.getResource("generated_api_reports")
                            ).toURI()).toFile().getAbsolutePath(),
                    "api_report." + reportType.toString().toLowerCase()
            );

            Files.write(
                    pdfPath,
                    Base64.getDecoder().decode(reportResponse.getReport())
            );

            if (reportType.toString().equalsIgnoreCase("pdf")) {
                PDDocument document = PDDocument.load(pdfPath.toFile());
                PDDocumentInformation info = document.getDocumentInformation();

                assertThat(info.getTitle()).isEqualTo(title);
                assertThat(info.getAuthor()).isEqualTo(author);
                assertThat(info.getSubject()).isEqualTo(subject);
                assertThat(info.getKeywords()).isEqualTo(keywords);
                assertThat(info.getCreator()).isEqualTo(creator);
            }
        }
    }

    @Test
    void testPdfProperties() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        String url = "http://localhost:" + port + "/report";

        PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setJavascript("app.alert({" +
                "cMsg: \"Test embedded JavaScript \", " +
                "cTitle: \"Reports API tests\" " +
                "});"
        );

        pdfProperties.setUserPassword("user password");
        pdfProperties.setOwnerPassword("owner password");
        pdfProperties.setPermissions(PdfWriter.ALLOW_COPY);

        ReportRequest reportRequest = getReportRequestForTests();
        reportRequest.setReportType(ReportRequest.ReportType.PDF);
        reportRequest.setPdfProperties(pdfProperties);

        ReportResponse reportResponse = restTemplate.postForObject(
                url,
                reportRequest,
                ReportResponse.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getReport()).isNotEmpty();
        assertThat(reportResponse.getMessage()).isEqualTo("");

        Path pdfPath = Paths.get(
                Paths.get(
                        Objects.requireNonNull(
                                classLoader.getResource("generated_api_reports")
                        ).toURI()).toFile().getAbsolutePath(),
                "api_with_properties_report.pdf"
        );

        Files.write(
                pdfPath,
                Base64.getDecoder().decode(reportResponse.getReport())
        );

    }

}
