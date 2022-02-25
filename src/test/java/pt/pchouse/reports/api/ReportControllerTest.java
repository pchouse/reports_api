package pt.pchouse.reports.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import pt.pchouse.reports.api.auth.Auth;
import pt.pchouse.reports.api.generator.Generator;
import pt.pchouse.reports.api.printer.Printer;
import pt.pchouse.reports.api.printer.PrinterException;
import pt.pchouse.reports.api.request.ReportRequest;
import pt.pchouse.reports.api.response.ReportResponse;
import pt.pchouse.reports.api.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportControllerTest {

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

    @MockBean
    private Generator generator;

    @MockBean
    private Auth auth;

    @MockBean
    private Printer printer;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void testGetReport() throws Exception {
        String url = "http://localhost:" + port + "/report";

        String report = "A base64 string encoded from report generator";
        Mockito.when(generator.generate()).thenReturn(report);
        Mockito.when(auth.isAuthorized()).thenReturn(true);

        ReportRequest reportRequest = new ReportRequest();

        ReportResponse reportResponse = restTemplate.postForObject(
                url,
                reportRequest,
                ReportResponse.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getReport()).isEqualTo(report);
        assertThat(reportResponse.getMessage()).isEqualTo("");
    }

    @Test
    void testGetReportAuthorized() {

        String url = "http://localhost:" + port + "/report";

        Mockito.when(auth.isAuthorized()).thenReturn(false);

        RequestEntity<ReportRequest> request = RequestEntity.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .body(new ReportRequest());

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assert reportResponse != null;
        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isEqualTo("Client not authorized");
        assertThat(reportResponse.getReport()).isNull();

    }

    @Test
    void testGetReportException() throws Exception {
        String url = "http://localhost:" + port + "/report";
        String msg = "Test throw exception";
        Mockito.when(generator.generate()).thenThrow(new Exception(msg));
        Mockito.when(auth.isAuthorized()).thenReturn(true);

        RequestEntity<ReportRequest> request = RequestEntity.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .body(new ReportRequest());

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assert reportResponse != null;

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getReport()).isNull();
        assertThat(reportResponse.getMessage()).isEqualTo(msg);
    }

    @Test
    void testErrorHandler() {

        Mockito.when(auth.isAuthorized()).thenReturn(true);

        String url = "http://localhost:" + port + "/report";
        RequestEntity<String> request = RequestEntity.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .body("");

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(500);
        assert reportResponse != null;
        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isNotEmpty();
        assertThat(reportResponse.getReport()).isNull();
        assertThat(reportResponse.getDuration()).isNull();
    }

    @Test
    void testMissingPath() {

        String url = "http://localhost:" + port + "/pathNotExist";
        RequestEntity<String> request = RequestEntity.post(url)
                .accept(MediaType.APPLICATION_JSON)
                .body("");

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        assert reportResponse != null;
        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isEqualTo("Action not found");
        assertThat(reportResponse.getReport()).isNull();
        assertThat(reportResponse.getDuration()).isNull();
    }

    @Test
    void testCutPaper() {
        String url = "http://localhost:" + port + "/cut";

        Mockito.when(auth.isAuthorized()).thenReturn(true);

        Response reportResponse = restTemplate.getForObject(
                url,
                Response.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getMessage()).isEqualTo("");
    }

    @Test
    void testCutPaperThrowError() throws Exception {
        String url = "http://localhost:" + port + "/cut";

        String msg = "Throw a error";
        Mockito.when(auth.isAuthorized()).thenReturn(true);
        Mockito.doThrow(new PrinterException(msg)).when(printer).cutPaper();

        RequestEntity<Void> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assert reportResponse != null;

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isEqualTo(msg);
    }

    @Test
    void testCutAndOpen() {
        String url = "http://localhost:" + port + "/cutandopen";

        Mockito.when(auth.isAuthorized()).thenReturn(true);

        Response reportResponse = restTemplate.getForObject(
                url,
                Response.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getMessage()).isEqualTo("");
    }

    @Test
    void testCutAndOpenThrowError() throws Exception {
        String url = "http://localhost:" + port + "/cutandopen";

        String msg = "Throw a error";
        Mockito.when(auth.isAuthorized()).thenReturn(true);
        Mockito.doThrow(new PrinterException(msg)).when(printer).cutAndCashDrawer();

        RequestEntity<Void> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assert reportResponse != null;

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isEqualTo(msg);
    }

    @Test
    void testCashDrawer() {
        String url = "http://localhost:" + port + "/cashdrawer";

        Mockito.when(auth.isAuthorized()).thenReturn(true);

        Response reportResponse = restTemplate.getForObject(
                url,
                Response.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getMessage()).isEqualTo("");
    }

    @Test
    void testVersion() {
        String url = "http://localhost:" + port + "/version";

        Mockito.when(auth.isAuthorized()).thenReturn(true);

        Response reportResponse = restTemplate.getForObject(
                url,
                Response.class
        );

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.OK);
        assertThat(reportResponse.getMessage()).isEqualTo("1.0.0");
    }

    @Test
    void testCashDrawerThrowError() throws Exception {
        String url = "http://localhost:" + port + "/cashdrawer";

        String msg = "Throw a error";
        Mockito.when(auth.isAuthorized()).thenReturn(true);
        Mockito.doThrow(new PrinterException(msg)).when(printer).cashDrawer();

        RequestEntity<Void> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<ReportResponse> responseEntity = restTemplate.exchange(
                request,
                ReportResponse.class
        );

        ReportResponse reportResponse = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode().value()).isEqualTo(400);
        assert reportResponse != null;

        assertThat(reportResponse.getStatus()).isEqualTo(ReportResponse.Status.ERROR);
        assertThat(reportResponse.getMessage()).isEqualTo(msg);
    }

}
