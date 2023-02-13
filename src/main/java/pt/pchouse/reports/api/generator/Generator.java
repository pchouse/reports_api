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

import com.itextpdf.text.Rectangle;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pt.pchouse.reports.api.printer.Printer;
import pt.pchouse.reports.api.printer.PrinterConfig;
import pt.pchouse.reports.api.request.*;
import rebelo.reports.core.*;
import rebelo.reports.core.datasource.*;
import rebelo.reports.core.sign.RRSignPdf;
import rebelo.reports.core.sign.RRSignPdfProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @since 1.0.0
 */
@Service
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class Generator implements IGenerator {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The tmp dir to create files defined in the application.properties
     * @since 1.0.0
     */
    @Value("${report.tmpdir}")
    private String configTmpDir;

    /**
     * The tmp dir to be used
     * @since 1.0.0
     */
    private Path tmpDir;

    /**
     *
     * @since 1.0.0
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * The report properties
     * @since 1.0.0
     */
    @Autowired
    private RRProperties properties;

    /**
     * The report request
     * @since 1.0.0
     */
    private final ReportRequest reportRequest;

    /**
     * Generated the report for the ReportRequest
     *
     * @param reportRequest The report request
     * @since 1.0.0
     */
    public Generator(ReportRequest reportRequest) {
        logger.debug("New instance of " + this.getClass().getName());
        this.reportRequest = reportRequest;
    }

    /**
     * Create the tmp dir where to write the temp files to generate the report
     *
     * @throws IOException If fail creating tmp dir
     * @since 1.0.0
     */
    private void createTmpDir() throws IOException {
        String prefix = "reports";
        if (StringUtils.isNotEmpty(configTmpDir)) {
            tmpDir = Files.createTempDirectory(
                    Paths.get(configTmpDir),
                    prefix
            );
            return;
        }
        tmpDir = Files.createTempDirectory(prefix);
    }

    /**
     * Delete the temporary directory and all files inside
     * @since 1.0.0
     */
    private void deleteTmpDir() {
        try {
            if (tmpDir == null) {
                return;
            }

            if (!Files.isDirectory(tmpDir, LinkOption.NOFOLLOW_LINKS)) {
                return;
            }

            for (File file : Objects.requireNonNull(tmpDir.toFile().listFiles())) {
                if (file.isFile()) {
                    logger.debug("Going to delete file {}", file.getAbsolutePath());
                    Files.delete(Paths.get(file.getAbsolutePath()));
                }
            }

            logger.debug("Going to delete directory {}", tmpDir.toFile().getAbsolutePath());
            Files.delete(tmpDir);
        } catch (Exception e) {
            logger.debug(
                    "Fail to delete temp directory '{}' with error: {}",
                    tmpDir.toFile().getAbsolutePath(),
                    e.getMessage()
            );
        }
    }

    /**
     *
     * @return The report as base64 encoded string or null if type is print
     * @throws Exception If error
     * @since 1.0.0
     */
    @Override
    public String generate() throws
            Exception {

        try {
            logger.debug("Start the report generator");

            createTmpDir();
            parseProperties();
            invokeReportExporter();

            if (properties.getType().equals(RRProperties.Types.print)) {
                return null;
            }

            return readAndEncodeGeneratedReport();

        } finally {
            CompletableFuture.supplyAsync(() -> {
                long init = (new Date()).getTime();
                do {
                    deleteTmpDir();
                } while (Files.exists(tmpDir) && ((new Date()).getTime() - init) < 3000);
                return null;
            });
        }
    }

    /**
     * Invoke the reposts core to create the report file or print the reports
     *
     * @throws NullNotAllowedException If properties report type is null
     * @since 1.0.0
     */
    private void invokeReportExporter() throws
            Exception {

        Report report = applicationContext.getBean(Report.class, properties);

        if (properties.getType().equals(RRProperties.Types.print)) {
            // Set the printer name
            PrinterConfig printerConfig = applicationContext.getBean(PrinterConfig.class);
            RRPrintProperties printProperties = (RRPrintProperties) properties.getTypeProperties();
            printProperties.setSelectedPrinter(printerConfig.getName());

            // print
            report.exportReport();

            int afterPrint = reportRequest.getAfterPrintOperations();

            if ((afterPrint & ReportRequest.AFTER_PRINT_CUT_PAPER) == ReportRequest.AFTER_PRINT_CUT_PAPER) {
                applicationContext.getBean(Printer.class).cutPaper();
            }

            if ((afterPrint & ReportRequest.AFTER_PRINT_OPEN_CASH_DRAWER) == ReportRequest.AFTER_PRINT_OPEN_CASH_DRAWER) {
                applicationContext.getBean(Printer.class).cashDrawer();
            }

            return;
        }

        @SuppressWarnings("rawtypes") Exporter exporter = report.getExporter();

        if (reportRequest.getMetadata() != null) {

            Class<?> parent = exporter.getClass();
            Field[] fields = exporter.getClass().getDeclaredFields();
            Optional<Field> exporterOutputOptional;
            do {
                exporterOutputOptional = Arrays.stream(fields).filter(
                        f -> f.getName().equals("exporterConfiguration")
                ).findFirst();

                if (exporterOutputOptional.isPresent()) {
                    Field exporterOutput = exporterOutputOptional.get();
                    exporterOutput.setAccessible(true);
                    parseMetadata(exporterOutput.get(exporter));

                } else {
                    parent = parent.getSuperclass();
                    if (parent != null) {
                        fields = parent.getDeclaredFields();
                    }
                }

            } while (parent != null && !exporterOutputOptional.isPresent());
        }

        report.exportReport();

        if (reportRequest.getReportType().equals(ReportRequest.ReportType.PDF) && reportRequest.getSign() != null) {

            RRSignPdf signPdf = new RRSignPdf(
                    report.getSignProp(),
                    report.getProperties().getOutputFile(),
                    report.getProperties().getOutputFile() + "_sign"
            );

            signPdf.signPdf();

            properties.setOutputFile(
                    properties.getOutputFile() + "_sign"
            );
        }

    }

    /**
     * Parse the documents metadata
     *
     * @param exporterOutput Exporter engine
     * @throws IllegalAccessException If field is not accessible
     * @since 1.0.0
     */
    private void parseMetadata(Object exporterOutput) throws IllegalAccessException {
        Metadata metadata = reportRequest.getMetadata();
        if (metadata == null || exporterOutput == null) {
            return;
        }

        for (Field field : exporterOutput.getClass().getDeclaredFields()) {
            Object value;
            switch (field.getName()) {
                case "metadataTitle":
                    value = metadata.getTitle();
                    break;
                case "metadataAuthor":
                    value = metadata.getAuthor();
                    break;
                case "metadataSubject":
                    value = metadata.getSubject();
                    break;
                case "metadataKeywords":
                    value = metadata.getKeywords();
                    break;
                case "metadataCreator":
                    value = metadata.getCreator();
                    break;
                case "displayMetadataTitle":
                    value = metadata.isDisplayMetadataTitle();
                    break;
                case "metadataApplication":
                    value = metadata.getApplication();
                    break;
                default:
                    continue;
            }
            field.setAccessible(true);
            field.set(exporterOutput, value == null ? "" : value);
        }
    }

    /**
     * Get the report request
     *
     * @return The report request
     * @since 1.0.0
     */
    @Override
    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    /**
     * Populate the property's parameters
     *
     * @throws NullNotAllowedException In case of parameter name or value null
     * @throws ParseException          Fail of date parse
     * @throws ParametersException     Fail of Parameter creation
     */
    private void parseParameters() throws NullNotAllowedException, ParseException, ParametersException {
        logger.debug("Start populate parameters");
        for (Parameter parameter : reportRequest.getParameters()) {
            switch (parameter.getType()) {
                case P_BIGDECIMAL:
                    properties.addParameter(
                            parameter.getName(),
                            new BigDecimal(parameter.getValue())
                    );
                    break;
                case P_BOOLEAN:
                case P_BOOL:
                    Boolean bool = Boolean.parseBoolean(parameter.getValue());
                    properties.addParameter(
                            parameter.getName(),
                            parameter.getType().equals(Parameter.Types.P_BOOLEAN) ?
                                    bool : bool.booleanValue()
                    );
                    break;
                case P_DATE:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(parameter.getFormat());
                    properties.addParameter(
                            parameter.getName(),
                            simpleDateFormat.parse(parameter.getValue())
                    );
                    break;
                case P_DOUBLE:
                    properties.addParameter(
                            parameter.getName(),
                            Double.valueOf(parameter.getValue())
                    );
                    break;
                case P_FLOAT:
                    properties.addParameter(
                            parameter.getName(),
                            Float.valueOf(parameter.getValue())
                    );
                    break;
                case P_INTEGER:
                    properties.addParameter(
                            parameter.getName(),
                            Integer.valueOf(parameter.getValue())
                    );
                    break;
                case P_LONG:
                    properties.addParameter(
                            parameter.getName(),
                            Long.valueOf(parameter.getValue())
                    );
                    break;
                case P_SHORT:
                    properties.addParameter(
                            parameter.getName(),
                            Short.valueOf(parameter.getValue())
                    );
                    break;
                case P_SQL_DATE:
                    properties.addParameter(
                            parameter.getName(),
                            java.sql.Date.valueOf(parameter.getValue())
                    );
                    break;
                case P_TIME:
                case P_SQL_TIME:
                    properties.addParameter(
                            parameter.getName(),
                            java.sql.Time.valueOf(parameter.getValue())
                    );
                    break;
                case P_TIMESTAMP:
                    properties.addParameter(
                            parameter.getName(),
                            java.sql.Timestamp.valueOf(parameter.getValue())
                    );
                    break;
                case P_STRING:
                    properties.addParameter(
                            parameter.getName(),
                            parameter.getValue()
                    );
                    break;
                default:
                    String msg = String.format(
                            "The parameter type '%s' not exist",
                            parameter.getType().toString()
                    );
                    logger.error(msg);
                    throw new ParametersException(msg);
            }//End switch

            logger.debug(
                    String.format(
                            "Parameter '%s' set to '%s'",
                            parameter.getName(),
                            properties.getParameters().get(parameter.getName()).toString()
                    )
            );
        }
    }

    /**
     * Parse the datasource
     *
     * @throws DataSourceException If fail the datasource parse
     * @since 1.0.0
     */
    private void parseDatasource() throws DataSourceException {
        logger.debug("Parsing datasource");
        try {

            if (reportRequest.getDatabase() != null) {
                RRDsDatabase dsDatabase = applicationContext.getBean(RRDsDatabase.class);
                dsDatabase.setConnString(reportRequest.getDatabase().getConnectionString());
                dsDatabase.setDriver(reportRequest.getDatabase().getDriver());
                dsDatabase.setUser(reportRequest.getDatabase().getUser());
                dsDatabase.setPassword(reportRequest.getDatabase().getPassword());
                properties.setDataSourceProperties(dsDatabase);
                logger.debug("Datasource set as database");
                return;

            }

            if (reportRequest.getJsonHttps() != null) {
                RRDsHttpsJson dsHttpsJson = applicationContext.getBean(RRDsHttpsJson.class);
                dsHttpsJson.setType(ARRDsHttp.Type.valueOf(reportRequest.getJsonHttps().getType().toString()));
                dsHttpsJson.setDatePattern(reportRequest.getJsonHttps().getDatePattern());
                dsHttpsJson.setNumberPattern(reportRequest.getJsonHttps().getNumberPattern());
                dsHttpsJson.setEncode(reportRequest.getEncoding());
                dsHttpsJson.setUrl(new URI(reportRequest.getJsonHttps().getUrl()).toURL());
                properties.setDataSourceProperties(dsHttpsJson);
                logger.debug("Datasource set as json https");
                return;
            }

            if (reportRequest.getJsonHttp() != null) {
                RRDsHttpJson dsHttpJson = applicationContext.getBean(RRDsHttpJson.class);
                dsHttpJson.setType(ARRDsHttp.Type.valueOf(reportRequest.getJsonHttp().getType().toString()));
                dsHttpJson.setDatePattern(reportRequest.getJsonHttp().getDatePattern());
                dsHttpJson.setNumberPattern(reportRequest.getJsonHttp().getNumberPattern());
                dsHttpJson.setEncode(reportRequest.getEncoding());
                dsHttpJson.setUrl(new URI(reportRequest.getJsonHttp().getUrl()).toURL());
                properties.setDataSourceProperties(dsHttpJson);
                logger.debug("Datasource set as json http");
                return;
            }

            if (reportRequest.getXmlHttps() != null) {
                RRDsHttpsXml dsHttpsXml = applicationContext.getBean(RRDsHttpsXml.class);
                dsHttpsXml.setType(ARRDsHttp.Type.valueOf(reportRequest.getXmlHttps().getType().toString()));
                dsHttpsXml.setDatePattern(reportRequest.getXmlHttps().getDatePattern());
                dsHttpsXml.setNumberPattern(reportRequest.getXmlHttps().getNumberPattern());
                dsHttpsXml.setEncode(reportRequest.getEncoding());
                dsHttpsXml.setUrl(new URI(reportRequest.getXmlHttps().getUrl()).toURL());
                properties.setDataSourceProperties(dsHttpsXml);
                logger.debug("Datasource set as xml https");
                return;
            }

            if (reportRequest.getXmlHttp() != null) {
                RRDsHttpXml dsHttpXml = applicationContext.getBean(RRDsHttpXml.class);
                dsHttpXml.setType(ARRDsHttp.Type.valueOf(reportRequest.getXmlHttp().getType().toString()));
                dsHttpXml.setDatePattern(reportRequest.getXmlHttp().getDatePattern());
                dsHttpXml.setNumberPattern(reportRequest.getXmlHttp().getNumberPattern());
                dsHttpXml.setEncode(reportRequest.getEncoding());
                dsHttpXml.setUrl(new URI(reportRequest.getXmlHttp().getUrl()).toURL());
                properties.setDataSourceProperties(dsHttpXml);
                logger.debug("Datasource set as xml http");
                return;
            }

            if (reportRequest.getJsonFile() != null) {
                RRDsFileJson dsFileJson = applicationContext.getBean(RRDsFileJson.class);
                dsFileJson.setDatePattern(reportRequest.getJsonFile().getDatePattern());
                dsFileJson.setNumberPattern(reportRequest.getJsonFile().getNumberPattern());
                Path path = createFileFromBase64EncodedString(
                        reportRequest.getJsonFile().getJson(), "datasource.json"
                );
                dsFileJson.setFile(path.toFile());
                properties.setDataSourceProperties(dsFileJson);
                logger.debug("Datasource set as json file");
                return;
            }

            if (reportRequest.getXmlFile() != null) {
                RRDsFileXml dsFileXml = applicationContext.getBean(RRDsFileXml.class);
                dsFileXml.setDatePattern(reportRequest.getXmlFile().getDatePattern());
                dsFileXml.setNumberPattern(reportRequest.getXmlFile().getNumberPattern());
                Path path = createFileFromBase64EncodedString(
                        reportRequest.getXmlFile().getXml(), "datasource.xml"
                );
                dsFileXml.setFile(path.toFile());
                properties.setDataSourceProperties(dsFileXml);
                logger.debug("Datasource set as xml file");
                return;
            }

        } catch (Exception ex) {
            String msgError = ex.getMessage();
            logger.error(msgError);
            throw new DataSourceException(msgError);
        }

        String msgError = "No datasource defined in Report Request";
        logger.error(msgError);
        throw new DataSourceException(msgError);

    }

    /**
     * Parse the pdf properties
     *
     * @throws pt.pchouse.reports.api.generator.ParseException If fail
     * @throws RRPropertiesException                           If no handler for type exists
     * @throws NullNotAllowedException                         If null on non null allow
     * @since 1.0.0
     */
    private void parsePdfProperties() throws pt.pchouse.reports.api.generator.ParseException, RRPropertiesException, NullNotAllowedException, MalformedURLException {

        RRPdfProperties pdfProperties = (RRPdfProperties) properties.getTypeProperties();
        pdfProperties.isSignPDF(reportRequest.getSign() != null);

        if (reportRequest.getPdfProperties() != null) {

            SimplePdfExporterConfiguration spec = pdfProperties.getSimplePdfExporterConfiguration();

            if (StringUtils.isNotEmpty(reportRequest.getPdfProperties().getUserPassword())) {
                spec.setUserPassword(reportRequest.getPdfProperties().getUserPassword());
            }

            if (StringUtils.isNotEmpty(reportRequest.getPdfProperties().getOwnerPassword())) {
                spec.setOwnerPassword(reportRequest.getPdfProperties().getOwnerPassword());
            }

            if (StringUtils.isNotEmpty(reportRequest.getPdfProperties().getJavascript())) {
                spec.setPdfJavaScript(reportRequest.getPdfProperties().getJavascript());
            }

            Integer permissions = reportRequest.getPdfProperties().getPermissions();
            spec.setPermissions(
                    permissions == null || permissions.equals(0) ?
                            PdfProperties.getAllowAllPermissions() :
                            permissions
            );
        }

        if (!pdfProperties.isSignPDF()) {
            return;
        }

        Sign reqSign = reportRequest.getSign();
        RRSignPdfProperties sign = applicationContext.getBean(RRSignPdfProperties.class);
        sign.setCertificateName(reqSign.getCertificateName());
        sign.setCertificatePassword(reqSign.getCertificatePassword());
        sign.setJavaKeyStorePassword(reqSign.getKeyStorePassword());

        Path extKeyStore = Paths.get(Paths.get("").toAbsolutePath().toString(), "keystore.ks");

        if (!Files.exists(extKeyStore)) {
            throw new pt.pchouse.reports.api.generator.ParseException(
                    String.format("Keystore file '%s' not found.", extKeyStore.toAbsolutePath())
            );
        }

        URL keystorePath = extKeyStore.toUri().toURL();

        sign.setJavaKeyStorePath(keystorePath.getFile());
        sign.setType(RRSignPdfProperties.Type.valueOf(reqSign.getCertificateType().toString()));
        sign.setLevel(RRSignPdfProperties.Level.valueOf(reqSign.getLevel().toString()));
        sign.setLocation(reqSign.getLocation());
        sign.setContact(reqSign.getContact());
        sign.isVisible(reqSign.isVisible());
        sign.setReazon(reqSign.getReason());

        if (reqSign.getSignRectangle() != null) {

            Rectangle rectangle = applicationContext.getBean(
                    Rectangle.class,
                    reqSign.getSignRectangle().getX() + reqSign.getSignRectangle().getWidth(),
                    reqSign.getSignRectangle().getY(),
                    reqSign.getSignRectangle().getRotation()
            );

            rectangle.setLeft(reqSign.getSignRectangle().getX());
            rectangle.setBottom(reqSign.getSignRectangle().getY() + reqSign.getSignRectangle().getHeight());

            sign.setRectangle(rectangle);
        }

        pdfProperties.setSignProp(sign);
    }

    /**
     * Parse DOCX properties
     * @since 1.0.0
     */
    private void parseDocxProperties() throws RRPropertiesException, NullNotAllowedException {
        ((RRDocxProperties) properties.getTypeProperties()).getSimpleDocxExporterConfiguration().setEmbedFonts(true);
    }

    /**
     * Parse DOCX properties
     * @since 1.0.0
     */
    private void parseHtmlProperties() {
        // RRHtmlProperties htmlProperties = (RRHtmlProperties) properties.getTypeProperties();
        // Todo future version create properties in request for html
        // htmlProperties.getSimpleHtmlExporterConfiguration();
    }

    /**
     * Parse ODS properties
     * @since 1.0.0
     */
    private void parseOdsProperties() {
        // RROdsProperties odsProperties = (RROdsProperties) properties.getTypeProperties();
        // Todo future version create properties in request for ods
        // odsProperties.getSimpleOdsExporterConfiguration();
    }

    /**
     * Parse PPTX properties
     * @since 1.0.0
     */
    private void parsePptxProperties() throws RRPropertiesException, NullNotAllowedException {
        RRPptxProperties pptxProperties = (RRPptxProperties) properties.getTypeProperties();
        pptxProperties.getSimplePptxExporterConfiguration().setEmbedFonts(true);
        // Todo future version create properties in request for pptx
        //pptxProperties.getSimplePptxExporterConfiguration().setSlideMasterReport();
    }

    /**
     * Parse XLS properties
     * @since 1.0.0
     */
    private void parseXlsProperties() {
        // RRXlsProperties xlsProperties = (RRXlsProperties) properties.getTypeProperties();
        // Todo future version create properties in request for xls
        // xlsProperties.getSimpleXlsReportConfiguration();
    }

    /**
     * Parse XLSX properties
     * @since 1.0.0
     */
    private void parseXlsxProperties() {
        // RRXlsxProperties xlsxProperties = (RRXlsxProperties) properties.getTypeProperties();
        // Todo future version create properties in request for xlsx
        // xlsxProperties.getSimpleXlsxExporterConfiguration();
    }

    /**
     * Parse PRINT properties
     * @since 1.0.0
     */
    private void parsePrintProperties() {
        // RRPrintProperties printProperties = (RRPrintProperties) properties.getTypeProperties();
        // Todo future version create properties in request for print
        // printProperties.getSimplePrintServiceExporterConfiguration();
    }

    /**
     * Parse the specific type report properties
     *
     * @throws NullNotAllowedException                         When properties are null
     * @throws RRPropertiesException                           When a properties error
     * @throws pt.pchouse.reports.api.generator.ParseException When parse error
     * @since 1.0.0
     */
    private void parseExporterProperties() throws NullNotAllowedException, RRPropertiesException, pt.pchouse.reports.api.generator.ParseException, MalformedURLException {

        switch (properties.getType()) {
            case pdf:
                this.parsePdfProperties();
                return;
            case docx:
                this.parseDocxProperties();
                return;
            case html:
                this.parseHtmlProperties();
                return;
            case ods:
                this.parseOdsProperties();
                return;
            case pptx:
                this.parsePptxProperties();
                return;
            case xls:
                this.parseXlsProperties();
                return;
            case xlsx:
                this.parseXlsProperties();
            default:
        }
    }

    /**
     * @throws IOException If fails create the file
     * @since 1.0.0
     */
    private void parseReportResources() throws IOException {
        if (reportRequest.getReportResources() == null) {
            return;
        }

        for (ReportResources reportResources : reportRequest.getReportResources()) {
            this.createFileFromBase64EncodedString(reportResources.getResource(), reportResources.getName());
        }
    }

    /**
     * Parse report properties
     *
     * @throws NullNotAllowedException If during the parse some field is null
     * @throws ParseException          If fail parsing
     * @throws ParametersException     If fail parse parameters
     * @throws RRPropertiesException   If fail parse RRProperties
     * @throws DataSourceException     If fail parse datasource
     * @since 1.0.0
     */
    private void parseProperties() throws
            NullNotAllowedException,
            ParseException,
            ParametersException,
            RRPropertiesException,
            DataSourceException,
            IOException,
            pt.pchouse.reports.api.generator.ParseException {

        parseParameters();
        parseDatasource();
        parseReportResources();

        properties.setType(RRProperties.Types.valueOf(reportRequest.getReportType().toString().toLowerCase()));
        properties.setCopies(reportRequest.getCopies());

        if (StringUtils.isNotEmpty(reportRequest.getEncoding())) {
            properties.setEncoding(reportRequest.getEncoding());
        } else {
            properties.setEncoding("UTF-8");
        }

        String jasper = createFileFromBase64EncodedString(reportRequest.getReport(), "report.jasper")
                .toFile()
                .getAbsolutePath();

        properties.setJasperFile(jasper);

        properties.setOutputFile(
                Paths.get(tmpDir.toFile().getAbsolutePath(), "output")
                        .toFile()
                        .getAbsolutePath()
        );

        parseExporterProperties();
    }

    /**
     * Create the file in the temporary directory
     *
     * @param base64   The base64 encoded string
     * @param fileName The file name
     * @return The created file path
     * @throws IOException If fail create or write to the file
     * @since 1.0.0
     */
    private Path createFileFromBase64EncodedString(String base64, String fileName) throws IOException {
        try {
            byte[] decode = Base64.getDecoder().decode(base64);
            return Files.write(
                    Paths.get(tmpDir.toFile().getAbsolutePath(), fileName),
                    decode,
                    StandardOpenOption.CREATE_NEW
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Get the report as base64 encoded string
     *
     * @return The generated report as a base64 encoded string
     * @throws NullNotAllowedException If fail read file
     * @throws IOException             If fail read file
     * @since 1.0.0
     */
    private String readAndEncodeGeneratedReport() throws NullNotAllowedException, IOException {
        byte[] report = Files.readAllBytes(Paths.get(properties.getOutputFile()));
        return Base64.getEncoder().encodeToString(report);
    }

}
