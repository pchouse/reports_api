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

package pt.pchouse.reports.api.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pt.pchouse.reports.Generic;
import pt.pchouse.reports.api.request.*;
import pt.pchouse.reports.api.request.datasource.Database;
import pt.pchouse.reports.api.request.datasource.HttpType;
import pt.pchouse.reports.api.request.datasource.JsonFile;
import pt.pchouse.reports.api.request.datasource.JsonHttps;
import rebelo.reports.core.NullNotAllowedException;
import rebelo.reports.core.RRPdfProperties;
import rebelo.reports.core.RRProperties;
import rebelo.reports.core.RRPropertiesException;
import rebelo.reports.core.datasource.*;
import rebelo.reports.core.sign.RRSignPdfProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class GeneratorTest extends Generic<Generator> {

    @BeforeAll
    public static void beforeAll() throws URISyntaxException, IOException {
        Path exportDir = Paths.get(
                Paths.get(
                        Objects.requireNonNull(
                                GeneratorTest.class.getClassLoader().getResource("sakila")
                        ).toURI()).getParent().toFile().getAbsolutePath(),
                "generated_reports"
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

    @Test
    @Override
    public void testAnnotations() {
        assertThat(Generator.class.isAnnotationPresent(Service.class)).isTrue();
        assertThat(Generator.class.isAnnotationPresent(Scope.class)).isTrue();
        assertThat(
                Generator.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();
    }

    @Test
    void testParseParameters() throws
            IOException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException,
            NoSuchFieldException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader().getResource("reportRequestParseParametersTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);
        Method populateParameters = generator.getClass().getDeclaredMethod("parseParameters");
        populateParameters.setAccessible(true);

        populateParameters.invoke(generator);

        Field propertiesField = generator.getClass().getDeclaredField("properties");
        propertiesField.setAccessible(true);

        RRProperties properties = (RRProperties) propertiesField.get(generator);

        properties.getParameters().forEach((name, value) -> {
            switch (name) {
                case "BIGDECIMAL":
                    assertThat(((BigDecimal) value).floatValue()).isEqualTo(9.99f);
                    break;
                case "BOOLEAN_TRUE":
                    assertThat(((Boolean) value).booleanValue()).isTrue();
                    break;
                case "BOOLEAN_FALSE":
                    assertThat(((Boolean) value).booleanValue()).isFalse();
                    break;
                case "BOOL_TRUE":
                    assertThat((boolean) value).isTrue();
                    break;
                case "BOOL_FALSE":
                    assertThat((boolean) value).isFalse();
                    break;
                case "DATE":
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    assertThat(sdfDate.format((Date) value)).isEqualTo("1969-10-05");
                    break;
                case "DATE_TIME":
                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    assertThat(sdfDateTime.format((Date) value)).isEqualTo("1969-10-05 09:19:29");
                    break;
                case "DOUBLE":
                    assertThat(((Double) value).floatValue()).isEqualTo(999.49f);
                    break;
                case "FLOAT":
                    assertThat(((Float) value).floatValue()).isEqualTo(99.29f);
                    break;
                case "LONG":
                    assertThat(((Long) value).intValue()).isEqualTo(9999);
                    break;
                case "INTEGER":
                    assertThat(((Integer) value).intValue()).isEqualTo(1999);
                    break;
                case "SHORT":
                    assertThat(((Short) value).intValue()).isEqualTo(9);
                    break;
                case "SQLDATE":
                    assertThat(((java.sql.Date) value).toLocalDate()).isEqualTo(
                            LocalDate.parse("1969-10-05")
                    );
                    break;
                case "TIME":
                case "SQLTIME":
                    assertThat(((java.sql.Time) value).toLocalTime()).isEqualTo(
                            LocalTime.parse("09:29:49")
                    );
                    break;
                case "TIMESTAMP":
                    assertThat(((java.sql.Timestamp) value).toLocalDateTime()).isEqualTo(
                            LocalDateTime.parse("1969-10-05T19:19:29",
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    );
                    break;
                case "STRING":
                    assertThat((String) value).isEqualTo("The string");
                    break;
                default:
                    Assertions.fail(
                            String.format("No assertion for parameter name '%s'", name)
                    );
            }
        });

    }

    @Test
    void testCreateAndDeleteTmpDirEmptyDefinition() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Generator generator = appContext.getBean(Generator.class, new ReportRequest());

        String systemTmpDir = System.getProperty("java.io.tmpdir");

        Field configTmpDirField = generator.getClass().getDeclaredField("configTmpDir");
        configTmpDirField.setAccessible(true);

        Field tmpDirField = generator.getClass().getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
        createTmpDirMethod.setAccessible(true);

        Method deleteTmpDirMethod = generator.getClass().getDeclaredMethod("deleteTmpDir");
        deleteTmpDirMethod.setAccessible(true);

        configTmpDirField.set(generator, "");
        createTmpDirMethod.invoke(generator);

        Path tmpDir = (Path) tmpDirField.get(generator);

        assertThat(Files.exists(tmpDir)).isTrue();

        assertThat(
                tmpDir.toFile().getParent()
        ).isEqualTo(systemTmpDir);

        assertThat(tmpDir.getFileName().toString().startsWith("reports")).isTrue();

        deleteTmpDirMethod.invoke(generator);

        assertThat(Files.exists(tmpDir)).isFalse();

    }

    @Test
    void testCreateAndDeleteTmpDirNullDefinition() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Generator generator = appContext.getBean(Generator.class, new ReportRequest());

        String systemTmpDir = System.getProperty("java.io.tmpdir");

        Field configTmpDirField = generator.getClass().getDeclaredField("configTmpDir");
        configTmpDirField.setAccessible(true);

        Field tmpDirField = generator.getClass().getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
        createTmpDirMethod.setAccessible(true);

        Method deleteTmpDirMethod = generator.getClass().getDeclaredMethod("deleteTmpDir");
        deleteTmpDirMethod.setAccessible(true);

        configTmpDirField.set(generator, null);
        createTmpDirMethod.invoke(generator);

        Path tmpDir = (Path) tmpDirField.get(generator);

        assertThat(Files.exists(tmpDir)).isTrue();
        assertThat(tmpDir.toFile().getParent()).isEqualTo(systemTmpDir);
        assertThat(tmpDir.getFileName().toString().startsWith("reports")).isTrue();

        deleteTmpDirMethod.invoke(generator);

        assertThat(Files.exists(tmpDir)).isFalse();

    }

    @Test
    void testCreateAndDeleteTmpDir() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {

        Generator generator = appContext.getBean(Generator.class, new ReportRequest());

        String testTmpDir = System.getProperty("java.io.tmpdir")
                + File.separator
                + "ReportsUnitTests";
        Files.createDirectory(Paths.get(testTmpDir));
        try {
            Field configTmpDirField = generator.getClass().getDeclaredField("configTmpDir");
            configTmpDirField.setAccessible(true);

            Field tmpDirField = generator.getClass().getDeclaredField("tmpDir");
            tmpDirField.setAccessible(true);

            Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
            createTmpDirMethod.setAccessible(true);

            Method deleteTmpDirMethod = generator.getClass().getDeclaredMethod("deleteTmpDir");
            deleteTmpDirMethod.setAccessible(true);

            configTmpDirField.set(generator, testTmpDir);
            createTmpDirMethod.invoke(generator);

            Path tmpDir = (Path) tmpDirField.get(generator);

            assertThat(Files.exists(tmpDir)).isTrue();
            assertThat(tmpDir.toFile().getParent()).isEqualTo(testTmpDir);
            assertThat(tmpDir.getFileName().toString().startsWith("reports")).isTrue();

            deleteTmpDirMethod.invoke(generator);

            assertThat(Files.exists(tmpDir)).isFalse();

        } finally {
            Files.delete(Paths.get(testTmpDir));
        }

    }

    @Test
    void testCreateFileFromBase64EncodedString() throws IOException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Generator generator = appContext.getBean(Generator.class, new ReportRequest());

        String testTmpDir = System.getProperty("java.io.tmpdir")
                + File.separator
                + "ReportsUnitTests";

        if (!Files.exists(Paths.get(testTmpDir))) {
            Files.createDirectory(Paths.get(testTmpDir));
        }

        try {
            Field configTmpDirField = generator.getClass().getDeclaredField("configTmpDir");
            configTmpDirField.setAccessible(true);

            Field tmpDirField = generator.getClass().getDeclaredField("tmpDir");
            tmpDirField.setAccessible(true);

            Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
            createTmpDirMethod.setAccessible(true);

            Method deleteTmpDirMethod = generator.getClass().getDeclaredMethod("deleteTmpDir");
            deleteTmpDirMethod.setAccessible(true);

            Method createFileFromBase64EncodedStringMethod = generator.getClass().getDeclaredMethod(
                    "createFileFromBase64EncodedString", String.class, String.class
            );

            createFileFromBase64EncodedStringMethod.setAccessible(true);

            configTmpDirField.set(generator, testTmpDir);
            createTmpDirMethod.invoke(generator);

            String string = "The string to encode";
            String base64 = Base64.getEncoder().encodeToString(string.getBytes());

            Path path = (Path) createFileFromBase64EncodedStringMethod.invoke(
                    generator,
                    new Object[]{base64, "base64TestFile"}
            );

            assertThat(Files.exists(path)).isTrue();
            String read = Files.readAllLines(path).get(0);
            assertThat(read).isEqualTo(string);

            deleteTmpDirMethod.invoke(generator);

            Path tmpDir = (Path) tmpDirField.get(generator);
            assertThat(Files.exists(tmpDir)).isFalse();

        } finally {
            try {
                Files.delete(Paths.get(testTmpDir));
            } catch (Exception e) {

            }
        }
    }

    @Test
    void testParseDatasourceDatabase() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseDataBaseTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
        Field propertiesField = generator.getClass().getDeclaredField("properties");

        parseDatasourceMethod.setAccessible(true);
        parseDatasourceMethod.invoke(generator);

        propertiesField.setAccessible(true);
        RRProperties properties = (RRProperties) propertiesField.get(generator);

        RRDsDatabase datasource = (RRDsDatabase) properties.getDataSourceProperties();

        assertThat(datasource.getDriver()).isEqualTo("The driver");
        assertThat(datasource.getUser()).isEqualTo("The user");
        assertThat(datasource.getConnString()).isEqualTo("The connection string");
        assertThat(datasource.getPassword()).isEqualTo("The password");

    }

    @Test
    void testParseDatasourceJsonHttps() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseJsonHttpsTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
        Field propertiesField = generator.getClass().getDeclaredField("properties");

        parseDatasourceMethod.setAccessible(true);
        parseDatasourceMethod.invoke(generator);

        propertiesField.setAccessible(true);
        RRProperties properties = (RRProperties) propertiesField.get(generator);

        RRDsHttpsJson datasource = (RRDsHttpsJson) properties.getDataSourceProperties();

        assertThat(datasource.getType().toString()).isEqualTo("GET");
        assertThat(datasource.getUrl().toString()).isEqualTo("https://api");
        assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
        assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

    }

    @Test
    void testParseDatasourceJsonHttp() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseJsonHttpTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
        Field propertiesField = generator.getClass().getDeclaredField("properties");

        parseDatasourceMethod.setAccessible(true);
        parseDatasourceMethod.invoke(generator);

        propertiesField.setAccessible(true);
        RRProperties properties = (RRProperties) propertiesField.get(generator);

        RRDsHttpJson datasource = (RRDsHttpJson) properties.getDataSourceProperties();

        assertThat(datasource.getType().toString()).isEqualTo("GET");
        assertThat(datasource.getUrl().toString()).isEqualTo("http://api");
        assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
        assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

    }

    @Test
    void testParseDatasourceXmlHttps() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseXmlHttpsTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
        Field propertiesField = generator.getClass().getDeclaredField("properties");

        parseDatasourceMethod.setAccessible(true);
        parseDatasourceMethod.invoke(generator);

        propertiesField.setAccessible(true);
        RRProperties properties = (RRProperties) propertiesField.get(generator);

        RRDsHttpsXml datasource = (RRDsHttpsXml) properties.getDataSourceProperties();

        assertThat(datasource.getType().toString()).isEqualTo("GET");
        assertThat(datasource.getUrl().toString()).isEqualTo("https://api");
        assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
        assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

    }

    @Test
    void testParseDatasourceXmlHttp() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseXmlHttpTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
        Field propertiesField = generator.getClass().getDeclaredField("properties");

        parseDatasourceMethod.setAccessible(true);
        parseDatasourceMethod.invoke(generator);

        propertiesField.setAccessible(true);
        RRProperties properties = (RRProperties) propertiesField.get(generator);

        RRDsHttpXml datasource = (RRDsHttpXml) properties.getDataSourceProperties();

        assertThat(datasource.getType().toString()).isEqualTo("GET");
        assertThat(datasource.getUrl().toString()).isEqualTo("http://api");
        assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
        assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

    }

    @Test
    void testParseDatasourceXmlFile() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseXmlFileTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
        createTmpDirMethod.setAccessible(true);

        createTmpDirMethod.invoke(generator);

        try {
            Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
            Field propertiesField = generator.getClass().getDeclaredField("properties");

            parseDatasourceMethod.setAccessible(true);
            parseDatasourceMethod.invoke(generator);

            propertiesField.setAccessible(true);
            RRProperties properties = (RRProperties) propertiesField.get(generator);

            RRDsFileXml datasource = (RRDsFileXml) properties.getDataSourceProperties();

            assertThat(datasource.getFile().getName()).isEqualTo("datasource.xml");
            assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
            assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

            Path path = Paths.get(datasource.getFile().getAbsolutePath());
            assertThat(Files.exists(path)).isTrue();

            String read = Files.readAllLines(path).get(0);
            assertThat(read).isEqualTo("The xml encoded string");
        } finally {
            try {
                Method deleteTmpDirMethod = generator.getClass().getMethod("deleteTmpDir");
                deleteTmpDirMethod.setAccessible(true);
                deleteTmpDirMethod.invoke(generator);
            } catch (Exception e) {

            }
        }
    }

    @Test
    void testParseDatasourceJsonFile() throws NoSuchMethodException, IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseJsonFileTest.json"),
                ReportRequest.class
        );

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
        createTmpDirMethod.setAccessible(true);

        createTmpDirMethod.invoke(generator);

        try {
            Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");
            Field propertiesField = generator.getClass().getDeclaredField("properties");

            parseDatasourceMethod.setAccessible(true);
            parseDatasourceMethod.invoke(generator);

            propertiesField.setAccessible(true);
            RRProperties properties = (RRProperties) propertiesField.get(generator);

            RRDsFileJson datasource = (RRDsFileJson) properties.getDataSourceProperties();

            assertThat(datasource.getFile().getName()).isEqualTo("datasource.json");
            assertThat(datasource.getDatePattern()).isEqualTo("yyyy-MM-dd");
            assertThat(datasource.getNumberPattern()).isEqualTo("#.##");

            Path path = Paths.get(datasource.getFile().getAbsolutePath());
            assertThat(Files.exists(path)).isTrue();

            String read = Files.readAllLines(path).get(0);
            assertThat(read).isEqualTo("The encoded json string");
        } finally {
            try {
                Method deleteTmpDirMethod = generator.getClass().getMethod("deleteTmpDir");
                deleteTmpDirMethod.setAccessible(true);
                deleteTmpDirMethod.invoke(generator);
            } catch (Exception e) {

            }
        }
    }

    @Test
    void testParseNoDatasource() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue("{}", ReportRequest.class);

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        try {
            Method parseDatasourceMethod = generator.getClass().getDeclaredMethod("parseDatasource");

            parseDatasourceMethod.setAccessible(true);
            parseDatasourceMethod.invoke(generator);

        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(DataSourceException.class);
            assertThat(e.getCause().getMessage()).isEqualTo("No datasource defined in Report Request");
            return;
        }

        fail("Should throw " + DataSourceException.class.getName());

    }

    @Test
    public void testParseProperties() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, RRPropertiesException, NullNotAllowedException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequest.json"),
                ReportRequest.class);

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        try {

            Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
            createTmpDirMethod.setAccessible(true);
            createTmpDirMethod.invoke(generator);

            Method parsePropertiesMethod = generator.getClass().getDeclaredMethod("parseProperties");
            parsePropertiesMethod.setAccessible(true);
            parsePropertiesMethod.invoke(generator);

            Field propertiesField = generator.getClass().getDeclaredField("properties");
            propertiesField.setAccessible(true);
            RRProperties properties = (RRProperties) propertiesField.get(generator);

            assertThat(properties.getTypeProperties()).isInstanceOf(RRPdfProperties.class);
            assertThat(properties.getType()).isEqualTo(RRProperties.Types.pdf);
            assertThat(properties.getParameters()).isNotEmpty();
            assertThat(properties.getCopies()).isEqualTo(9);
            assertThat(properties.getEncoding()).isEqualTo("utf-8");

            RRPdfProperties pdfProperties = (RRPdfProperties) properties.getTypeProperties();
            assertThat(pdfProperties.isSignPDF()).isTrue();
            assertThat(pdfProperties.getSignProp().isVisible()).isTrue();
            assertThat(pdfProperties.getSignProp().getJavaKeyStorePassword()).isEqualTo("password");
            assertThat(pdfProperties.getSignProp().getCertificateName()).isEqualTo("rreports");
            assertThat(pdfProperties.getSignProp().getLevel()).isEqualTo(RRSignPdfProperties.Level.CERTIFIED_NO_CHANGES_ALLOWED);
            assertThat(pdfProperties.getSignProp().getType()).isEqualTo(RRSignPdfProperties.Type.SELF);

            assertThat(pdfProperties.getSignProp().getRectangle().getLeft()).isEqualTo(99f);
            assertThat(pdfProperties.getSignProp().getRectangle().getBottom()).isEqualTo(9f + 399f);
            assertThat(pdfProperties.getSignProp().getRectangle().getRight()).isEqualTo(99f + 299f);
            assertThat(pdfProperties.getSignProp().getRectangle().getTop()).isEqualTo(9f);
            assertThat(pdfProperties.getSignProp().getRectangle().getRotation()).isEqualTo(90);

            assertThat(pdfProperties.getSignProp().getLocation()).isEqualTo("Lisbon");
            assertThat(pdfProperties.getSignProp().getReazon()).isEqualTo("Reports API test");
            assertThat(pdfProperties.getSignProp().getContact()).isEqualTo("The contact");

            SimplePdfExporterConfiguration spec = pdfProperties.getSimplePdfExporterConfiguration();
            assertThat(spec.getUserPassword()).isEqualTo("user password");
            assertThat(spec.getOwnerPassword()).isEqualTo("owner password");
            assertThat(spec.getPdfJavaScript()).isEqualTo("javascript");
            assertThat(spec.getPermissions()).isEqualTo(999);

        } finally {
            try {
                Method deleteTmpDirMethod = generator.getClass().getMethod("deleteTmpDir");
                deleteTmpDirMethod.setAccessible(true);
                deleteTmpDirMethod.invoke(generator);
            } catch (Exception e) {

            }
        }

    }

    /**
     * @return The report request
     * @throws Exception On failure
     */
    private ReportRequest getBaseReportRequestForTest() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();

        Properties dbProp = new Properties();
        dbProp.load(classLoader.getResourceAsStream("sakila/mysql.properties"));

        Database database = appContext.getBean(
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

        ReportRequest request = appContext.getBean(ReportRequest.class);
        request.setReport(base64Jasper);

        request.setDatabase(database);
        ArrayList<Parameter> parameters = new ArrayList<>();
        request.setParameters(parameters);

        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_STRING, "P_STRING", "Parameter String"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BOOLEAN, "P_BOOLEAN", "true"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DOUBLE, "P_DOUBLE", "999.49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_FLOAT, "P_FLOAT", "9.09"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_INTEGER, "P_INTEGER", "9"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_LONG, "P_LONG", "49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SHORT, "P_SHORT", "109"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BIGDECIMAL, "P_BIG_DECIMAL", "1999.2999"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_DATE, "P_SQL_DATE", "1969-10-05"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_TIME, "P_SQL_TIME", "09:29:49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_TIMESTAMP, "P_TIMESTAMPT", "1969-10-05 19:19:29"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DATE, "P_DATE", "1969-10-05", "yyyy-MM-dd"));

        return request;
    }

    @Test
    void testInvokeReportExporter() throws Exception {

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path exportDir = Paths.get(
                Paths.get(
                        Objects.requireNonNull(
                                GeneratorTest.class.getClassLoader().getResource("sakila")
                        ).toURI()).getParent().toFile().getAbsolutePath(),
                "generated_reports"
        );

        for (ReportRequest.ReportType type : ReportRequest.ReportType.values()) {

            if (type.equals(ReportRequest.ReportType.PRINT)) {
                continue;
            }

            ReportRequest request = getBaseReportRequestForTest();
            request.setReportType(type);

            Generator generator = appContext.getBean(Generator.class, request);

            String report = generator.generate();

            assertThat(report).isNotEmpty();

            Files.write(
                    Paths.get(exportDir.toFile().getAbsolutePath(), "test_report." + type.toString().toLowerCase()),
                    Base64.getDecoder().decode(report)
            );

            Path tmpDir = (Path) tmpDirField.get(generator);

            long init = (new Date()).getTime();
            //noinspection StatementWithEmptyBody
            while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;
            assertThat(Files.exists(tmpDir)).isFalse();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    void testInvokeReportExporterPdfSign() throws Exception {

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path exportDir = Paths.get(
                Paths.get(
                        Objects.requireNonNull(
                                GeneratorTest.class.getClassLoader().getResource("sakila")
                        ).toURI()).getParent().toFile().getAbsolutePath(),
                "generated_reports"
        );

        ReportRequest request = getBaseReportRequestForTest();
        request.setReportType(ReportRequest.ReportType.PDF);

        Sign sign = new Sign();
        sign.setLocation("Lisbon");
        sign.setLevel(Sign.Level.CERTIFIED_NO_CHANGES_ALLOWED);
        sign.setCertificateType(Sign.CertificateType.SELF);
        sign.setCertificateName("rreports");
        sign.setCertificatePassword("password");
        sign.setKeyStorePassword("password");
        sign.setVisible(true);
        sign.setReason("Api test");
        sign.setContact("The contatct");

        SignRectangle rectangle = new SignRectangle();
        rectangle.setX(300);
        rectangle.setY(500);
        rectangle.setHeight(99);
        rectangle.setWidth(99);

        sign.setSignRectangle(rectangle);
        request.setSign(sign);

        Generator generator = appContext.getBean(Generator.class, request);

        String report = generator.generate();

        assertThat(report).isNotEmpty();

        Files.write(
                Paths.get(exportDir.toFile().getAbsolutePath(), "test_report_signed.pdf"),
                Base64.getDecoder().decode(report)
        );

        Path tmpDir = (Path) tmpDirField.get(generator);

        long init = (new Date()).getTime();
        while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;

        assertThat(Files.exists(tmpDir)).isFalse();
    }

    @Test
    void testParseSubReports() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        ObjectMapper mapper = new ObjectMapper();
        ReportRequest reportRequest = mapper.readValue(
                this.getClass().getClassLoader()
                        .getResource("reportRequestParseSubReport.json"),
                ReportRequest.class);

        Generator generator = appContext.getBean(Generator.class, reportRequest);

        try {

            Method createTmpDirMethod = generator.getClass().getDeclaredMethod("createTmpDir");
            createTmpDirMethod.setAccessible(true);
            createTmpDirMethod.invoke(generator);

            Method parseSubReports = generator.getClass().getDeclaredMethod("parseReportResources");
            parseSubReports.setAccessible(true);
            parseSubReports.invoke(generator);

            Field tmpDirField = generator.getClass().getDeclaredField("tmpDir");
            tmpDirField.setAccessible(true);
            Path tmpDir = (Path) tmpDirField.get(generator);

            File[] files = tmpDir.toFile().listFiles();
            assert files != null;
            assertThat(Arrays.stream(files).count()).isEqualTo(2L);

            for (File file : files) {

                String fileContent = Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0);
                if (file.getName().equals("report_1.jasper")) {
                    assertThat(fileContent).isEqualTo("The encoded jasper report 1");
                } else if (file.getName().equals("report_2.jasper")) {
                    assertThat(fileContent).isEqualTo("The encoded jasper report 2");
                } else {
                    fail("Unknown file {}", file.getName());
                }
            }

        } finally {
            try {
                Method deleteTmpDirMethod = generator.getClass().getMethod("deleteTmpDir");
                deleteTmpDirMethod.setAccessible(true);
                deleteTmpDirMethod.invoke(generator);
            } catch (Exception e) {

            }
        }
    }

    @Test
    void testSubReport() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();

        Properties dbProp = new Properties();
        dbProp.load(classLoader.getResourceAsStream("sakila/mysql.properties"));

        Database database = appContext.getBean(
                Database.class,
                dbProp.getProperty("connection"),
                dbProp.getProperty("driver"),
                dbProp.getProperty("user"),
                dbProp.getProperty("password")
        );

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path exportDir = Paths.get(
                Objects.requireNonNull(classLoader.getResource("generated_reports")).toURI()
        );

        ReportRequest request = appContext.getBean(ReportRequest.class);
        request.setReportResources(new ArrayList<>());

        URL jasperPath = classLoader.getResource("testsubreport/teste_subreport.jasper");
        if (jasperPath == null) {
            throw new Exception("teste_subreport.jasper not load");
        }

        URL subReportOnePath = classLoader.getResource("testsubreport/subreport_1.jasper");
        if (subReportOnePath == null) {
            throw new Exception("subreport_1.jasper not load");
        }

        URL subReportTwoPath = classLoader.getResource("testsubreport/subreport_2.jasper");
        if (subReportTwoPath == null) {
            throw new Exception("subreport_2.jasper not load");
        }

        request.setReport(
                Base64.getEncoder().encodeToString(
                        Files.readAllBytes(
                                Paths.get(jasperPath.toURI())
                        )
                )
        );

        ReportResources reportResourcesOne = new ReportResources();
        reportResourcesOne.setName("subreport_1.jasper");
        reportResourcesOne.setResource(
                Base64.getEncoder().encodeToString(
                        Files.readAllBytes(
                                Paths.get(subReportOnePath.toURI())
                        )
                )
        );

        request.getReportResources().add(reportResourcesOne);

        ReportResources reportResourcesTwo = new ReportResources();
        reportResourcesTwo.setName("subreport_2.jasper");
        reportResourcesTwo.setResource(
                Base64.getEncoder().encodeToString(
                        Files.readAllBytes(
                                Paths.get(subReportTwoPath.toURI())
                        )
                )
        );

        request.getReportResources().add(reportResourcesTwo);

        request.setDatabase(database);
        ArrayList<Parameter> parameters = new ArrayList<>();
        request.setParameters(parameters);
        request.setReportType(ReportRequest.ReportType.PDF);

        Generator generator = appContext.getBean(Generator.class, request);

        String report = generator.generate();

        assertThat(report).isNotEmpty();

        Files.write(
                Paths.get(exportDir.toFile().getAbsolutePath(), "test_sub_report.pdf"),
                Base64.getDecoder().decode(report)
        );

        Path tmpDir = (Path) tmpDirField.get(generator);

        long init = (new Date()).getTime();
        //noinspection StatementWithEmptyBody
        while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;
        assertThat(Files.exists(tmpDir)).isFalse();

    }

    @Test
    void testJsonFileDatasource() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path jasperReportPath = Paths.get(
                Objects.requireNonNull(classLoader.getResource("jsondatasource/report_json_file.jasper")).toURI()
        );

        String jasperFile = Base64.getEncoder().encodeToString(
                Files.readAllBytes(jasperReportPath)
        );

        Path exportDir = Paths.get(
                Objects.requireNonNull(classLoader.getResource("generated_reports")).toURI()
        );

        URL jsonUrl = classLoader.getResource("jsondatasource/json_datasource.json");

        JsonFile jsonFile = new JsonFile();
        assert jsonUrl != null;
        jsonFile.setJson(
                Base64.getEncoder().encodeToString(
                        Files.readAllBytes(
                                Paths.get(jsonUrl.toURI())
                        )
                )
        );

        ReportRequest request = appContext.getBean(ReportRequest.class);
        request.setJsonFile(jsonFile);
        request.setReportType(ReportRequest.ReportType.PDF);
        request.setParameters(new ArrayList<>());
        request.setReport(jasperFile);

        Generator generator = appContext.getBean(Generator.class);
        String report = generator.generate();

        assertThat(report).isNotEmpty();

        Files.write(
                Paths.get(exportDir.toFile().getAbsolutePath(), "test_json_file_datasource.pdf"),
                Base64.getDecoder().decode(report)
        );

        Path tmpDir = (Path) tmpDirField.get(generator);

        long init = (new Date()).getTime();
        //noinspection StatementWithEmptyBody
        while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;
        assertThat(Files.exists(tmpDir)).isFalse();

    }

    @Test
    void testJsonHttpsDatasource() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path jasperReportPath = Paths.get(
                Objects.requireNonNull(classLoader.getResource("jsondatasource/report_json_https.jasper")).toURI()
        );

        String jasperFile = Base64.getEncoder().encodeToString(
                Files.readAllBytes(jasperReportPath)
        );

        Path exportDir = Paths.get(
                Objects.requireNonNull(classLoader.getResource("generated_reports")).toURI()
        );

        JsonHttps jsonHttps = new JsonHttps();
        jsonHttps.setType(HttpType.GET);
        jsonHttps.setUrl("https://jsonplaceholder.typicode.com/users");

        ReportRequest request = appContext.getBean(ReportRequest.class);
        request.setJsonHttps(jsonHttps);
        request.setReportType(ReportRequest.ReportType.PDF);
        request.setParameters(new ArrayList<>());
        request.setReport(jasperFile);

        Generator generator = appContext.getBean(Generator.class);
        String report = generator.generate();

        assertThat(report).isNotEmpty();

        Files.write(
                Paths.get(exportDir.toFile().getAbsolutePath(), "test_json_https_datasource.pdf"),
                Base64.getDecoder().decode(report)
        );

        Path tmpDir = (Path) tmpDirField.get(generator);

        long init = (new Date()).getTime();
        //noinspection StatementWithEmptyBody
        while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;
        assertThat(Files.exists(tmpDir)).isFalse();

    }

    @Test
    @Disabled()
    void testTicket() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();

        Properties dbProp = new Properties();
        dbProp.load(classLoader.getResourceAsStream("sakila/mysql.properties"));

        Database database = appContext.getBean(
                Database.class,
                dbProp.getProperty("connection"),
                dbProp.getProperty("driver"),
                dbProp.getProperty("user"),
                dbProp.getProperty("password")
        );

        Field tmpDirField = Generator.class.getDeclaredField("tmpDir");
        tmpDirField.setAccessible(true);

        Path jasperReportPath = Paths.get(
                Objects.requireNonNull(classLoader.getResource("ticket/ticket.jasper")).toURI()
        );

        String jasperFile = Base64.getEncoder().encodeToString(
                Files.readAllBytes(jasperReportPath)
        );

        ReportRequest request = appContext.getBean(ReportRequest.class);
        request.setDatabase(database);
        request.setReportType(ReportRequest.ReportType.PRINT);
        request.setParameters(new ArrayList<>());
        request.setReport(jasperFile);
        request.setAfterPrintOperations(
                ReportRequest.AFTER_PRINT_CUT_PAPER
        );

        Generator generator = appContext.getBean(Generator.class);
        String report = generator.generate();

        assertThat(report).isNull();

        Path tmpDir = (Path) tmpDirField.get(generator);

        long init = (new Date()).getTime();
        //noinspection StatementWithEmptyBody
        while (Files.exists(tmpDir) && ((new Date()).getTime() - init < 3000)) ;
        assertThat(Files.exists(tmpDir)).isFalse();
    }

    @Test
    void testParsePdfMetadata() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        SimplePdfExporterConfiguration spec = new SimplePdfExporterConfiguration();

        Metadata metadata = new Metadata();

        String title = "The title";
        String author = "The author";
        String subject = "The subject";
        String keywords = "The keywords";
        String application = "The application";
        String creator = "The creator";

        metadata.setTitle(title);
        metadata.setAuthor(author);
        metadata.setSubject(subject);
        metadata.setKeywords(keywords);
        metadata.setCreator(creator);
        metadata.setApplication(application);

        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setMetadata(metadata);

        Generator generator = new Generator(reportRequest);

        Method parseMetadata = generator.getClass().getDeclaredMethod("parseMetadata", Object.class);
        parseMetadata.setAccessible(true);
        parseMetadata.invoke(generator, spec);

        assertThat(spec.getMetadataTitle()).isEqualTo(metadata.getTitle());
        assertThat(spec.getMetadataAuthor()).isEqualTo(metadata.getAuthor());
        assertThat(spec.getMetadataSubject()).isEqualTo(metadata.getSubject());
        assertThat(spec.getMetadataKeywords()).isEqualTo(metadata.getKeywords());
        assertThat(spec.getMetadataCreator()).isEqualTo(metadata.getCreator());
    }

    @Test
    void testParseDocxMetadata() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        SimpleDocxExporterConfiguration spec = new SimpleDocxExporterConfiguration();

        Metadata metadata = new Metadata();

        String title = "The title";
        String author = "The author";
        String subject = "The subject";
        String keywords = "The keywords";
        String application = "The application";
        String creator = "The creator";

        metadata.setTitle(title);
        metadata.setAuthor(author);
        metadata.setSubject(subject);
        metadata.setKeywords(keywords);
        metadata.setCreator(creator);
        metadata.setApplication(application);

        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setMetadata(metadata);

        Generator generator = new Generator(reportRequest);

        Method parseMetadata = generator.getClass().getDeclaredMethod("parseMetadata", Object.class);
        parseMetadata.setAccessible(true);
        parseMetadata.invoke(generator, spec);

        assertThat(spec.getMetadataTitle()).isEqualTo(metadata.getTitle());
        assertThat(spec.getMetadataAuthor()).isEqualTo(metadata.getAuthor());
        assertThat(spec.getMetadataSubject()).isEqualTo(metadata.getSubject());
        assertThat(spec.getMetadataKeywords()).isEqualTo(metadata.getKeywords());
        assertThat(spec.getMetadataApplication()).isEqualTo(metadata.getApplication());
    }

}
