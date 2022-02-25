/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package pt.pchouse.reports.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.reports.Generic;
import pt.pchouse.reports.api.request.datasource.*;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReportRequestTest extends Generic<ReportRequest> {

    @Override
    @Test
    @SuppressWarnings("all")
    public void testAnnotations() {
        assertThat(getTypeClass().isAnnotationPresent(Component.class)).isTrue();
        assertThat(getTypeClass().isAnnotationPresent(RequestScope.class)).isTrue();
        assertThat(
                ReportRequest.class.getAnnotation(JsonIgnoreProperties.class).ignoreUnknown()
        ).isTrue();
    }

    @Test
    void testBean() {

        ReportRequest reportRequest = appContext.getBean(ReportRequest.class);

        assertThat(reportRequest.getCopies()).isEqualTo(1);
        assertThat(reportRequest.getEncoding()).isEqualTo("UTF-8");
        assertThat(reportRequest.getAfterPrintOperations()).isEqualTo(0);

        String report = "Some base64 encoded string";
        ReportRequest.ReportType reportType = ReportRequest.ReportType.PDF;
        Database database = new Database();
        JsonHttp jsonHttp = new JsonHttp();
        JsonHttps jsonHttps = new JsonHttps();
        XmlHttp xmlHttp = new XmlHttp();
        XmlHttps xmlHttps = new XmlHttps();
        JsonFile jsonFile = new JsonFile();
        XmlFile xmlFile = new XmlFile();
        Sign sign = new Sign();
        int copies = 9;
        String encoding = "iso-8859-1";
        ArrayList<ReportResources> reportResources = new ArrayList<>();
        int afterPrint = 3;
        Metadata metadata = new Metadata();
        PdfProperties pdfProperties = new PdfProperties();

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter());

        reportRequest.setReport(report);
        reportRequest.setReportType(reportType);
        reportRequest.setDatabase(database);
        reportRequest.setJsonHttp(jsonHttp);
        reportRequest.setJsonHttps(jsonHttps);
        reportRequest.setXmlHttp(xmlHttp);
        reportRequest.setXmlHttps(xmlHttps);
        reportRequest.setJsonFile(jsonFile);
        reportRequest.setXmlFile(xmlFile);
        reportRequest.setSign(sign);
        reportRequest.setParameters(parameters);
        reportRequest.setCopies(copies);
        reportRequest.setEncoding(encoding);
        reportRequest.setReportResources(reportResources);
        reportRequest.setAfterPrintOperations(afterPrint);
        reportRequest.setMetadata(metadata);
        reportRequest.setPdfProperties(pdfProperties);

        assertThat(reportRequest.getReport()).isEqualTo(report);
        assertThat(reportRequest.getReportType()).isEqualTo(reportType);
        assertThat(reportRequest.getDatabase()).isEqualTo(database);
        assertThat(reportRequest.getJsonHttp()).isEqualTo(jsonHttp);
        assertThat(reportRequest.getJsonHttps()).isEqualTo(jsonHttps);
        assertThat(reportRequest.getXmlHttp()).isEqualTo(xmlHttp);
        assertThat(reportRequest.getXmlHttps()).isEqualTo(xmlHttps);
        assertThat(reportRequest.getJsonFile()).isEqualTo(jsonFile);
        assertThat(reportRequest.getXmlFile()).isEqualTo(xmlFile);
        assertThat(reportRequest.getSign()).isEqualTo(sign);
        assertThat(reportRequest.getParameters()).isEqualTo(parameters);
        assertThat(reportRequest.getCopies()).isEqualTo(copies);
        assertThat(reportRequest.getReportResources()).isEqualTo(reportResources);
        assertThat(reportRequest.getAfterPrintOperations()).isEqualTo(afterPrint);
        assertThat(reportRequest.getMetadata()).isEqualTo(metadata);
        assertThat(reportRequest.getPdfProperties()).isEqualTo(pdfProperties);
    }

}
