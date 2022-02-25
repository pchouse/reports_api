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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.reports.api.request.datasource.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component
@RequestScope
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ReportRequest {

    /**
     * Cut the paper after print the document
     * @since 1.0.0
     */
    public static final int AFTER_PRINT_CUT_PAPER = 1;

    /**
     * Open the cash drawer after print the document
     * @since 1.0.0
     */
    public static final int AFTER_PRINT_OPEN_CASH_DRAWER = 2;

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The type of request
     * @since 1.0.0
     */
    public enum ReportType {
        CSV,
        DOCX,
        HTML,
        JSON,
        ODS,
        ODT,
        PDF,
        PPTX,
        RTF,
        TEXT,
        XLS,
        XLSX,
        XML,
        PRINT
    }

    /**
     * The JasperReports file, the compiled jrxml file (.jasper) as base64 encoded
     * @since 1.0.0
     */
    private String report;

    /**
     * The JasperReports sub reports files
     * @since 1.0.0
     */
    private ArrayList<ReportResources> reportResources;

    /**
     * The type of report that should be generated.
     * @since 1.0.0
     */
    private ReportType reportType;

    /**
     * The list of report parameters
     * @since 1.0.0
     */
    private ArrayList<Parameter> parameters;

    /**
     * Database as datasource
     * @since 1.0.0
     */
    private Database database;

    /**
     * Json http as datasource
     * @since 1.0.0
     */
    private JsonHttp jsonHttp;

    /**
     * Json Https as datasource
     * @since 1.0.0
     */
    private JsonHttps jsonHttps;

    /**
     * Xml http datasource
     * @since 1.0.0
     */
    private XmlHttp xmlHttp;

    /**
     * Xml https datasource
     * @since 1.0.0
     */
    private XmlHttps xmlHttps;

    /**
     * Json file datasource
     * @since 1.0.0
     */
    private JsonFile jsonFile;

    /**
     * Xml file datasource
     * @since 1.0.0
     */
    private XmlFile xmlFile;

    /**
     * The pdf signature properties
     * @since 1.0.0
     */
    private Sign sign;

    /**
     * Number of report copies
     * @since 1.0.0
     */
    private int copies = 1;

    /**
     * The report encoding
     * @since 1.0.0
     */
    private String encoding = "UTF-8";

    /**
     * Bitwise operations after send the report to the printer
     * Only apply to printer mode
     * @since 1.0.0
     */
    private int afterPrintOperations = 0;

    /**
     * Document metadata
     * @since 1.0.0
     */
    private Metadata metadata;

    /**
     * Pdf properties
     * @since 1.0.0
     */
    private PdfProperties pdfProperties;

    /**
     * Report request definition
     * @since 1.0.0
     */
    public ReportRequest() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * The base64 encoded jasper report (jasper file compiled from jrxml file)
     *
     * @return The the base64 encoded jasper report
     * @since 1.0.0
     */
    public String getReport() {
        return report;
    }

    /**
     * The base64 encoded jasper report (jasper file compiled from jrxml file)
     *
     * @param report The the base64 encoded jasper report
     * @since 1.0.0
     */
    public void setReport(String report) {
        this.report = report;
        logger.debug("Report was set");
    }

    /**
     * The type of report that should be generated.
     *
     * @return The type
     * @since 1.0.0
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * The type of report that should be generated.
     *
     * @param reportType The type
     * @since 1.0.0
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
        logger.debug("ReportType set to {}", this.reportType == null ? "null" : this.reportType.toString());
    }

    /**
     * The report parameters
     *
     * @return The list of parameters
     * @since 1.0.0
     */
    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    /**
     * The report parameters
     *
     * @param parameters The list of parameters
     * @since 1.0.0
     */
    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
        logger.debug("Parameters set to {}", this.parameters == null ? "null" : "an list");
    }

    /**
     * Database datasource properties
     *
     * @return The properties
     * @since 1.0.0
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Database datasource properties
     *
     * @param database The properties
     * @since 1.0.0
     */
    public void setDatabase(Database database) {
        this.database = database;
        logger.debug("Database set to {}", this.database == null ? "null" : this.database.toString());
    }

    /**
     * Json http datasource properties
     *
     * @return The properties
     * @since 1.0.0
     */
    public JsonHttp getJsonHttp() {
        return jsonHttp;
    }

    /**
     * Json http datasource properties
     *
     * @param jsonHttp The properties
     * @since 1.0.0
     */
    public void setJsonHttp(JsonHttp jsonHttp) {
        this.jsonHttp = jsonHttp;
        logger.debug("JsonHttp set to {}", this.jsonHttp == null ? "null" : this.jsonHttp.toString());
    }

    /**
     * Json https datasource properties
     *
     * @return The properties
     * @since 1.0.0
     */
    public JsonHttps getJsonHttps() {
        return jsonHttps;
    }

    /**
     * Json https datasource properties
     *
     * @param jsonHttps The properties
     * @since 1.0.0
     */
    public void setJsonHttps(JsonHttps jsonHttps) {
        this.jsonHttps = jsonHttps;
        logger.debug("JsonHttps set to {}", this.jsonHttps == null ? "null" : this.jsonHttps.toString());
    }

    /**
     * Xml http datasource properties
     *
     * @return The properties
     * @since 1.0.0
     */
    public XmlHttp getXmlHttp() {
        return xmlHttp;
    }

    /**
     * Xml https datasource properties
     *
     * @param xmlHttp The properties
     * @since 1.0.0
     */
    public void setXmlHttp(XmlHttp xmlHttp) {
        this.xmlHttp = xmlHttp;
        logger.debug("XmlHttp set to {}", this.xmlHttp == null ? "null" : this.xmlHttp.toString());
    }

    /**
     * Xml https datasource properties
     *
     * @return The properties
     * @since 1.0.0
     */
    public XmlHttps getXmlHttps() {
        return xmlHttps;
    }

    /**
     * Xml https datasource properties
     *
     * @param xmlHttps The properties
     * @since 1.0.0
     */
    public void setXmlHttps(XmlHttps xmlHttps) {
        this.xmlHttps = xmlHttps;
        logger.debug("XmlHttps set to {}", this.xmlHttps == null ? "null" : this.xmlHttps.toString());
    }

    /**
     * Get JsonFile datasource
     *
     * @return The JsonFile datasource
     * @since 1.0.0
     */
    public JsonFile getJsonFile() {
        return jsonFile;
    }

    /**
     * Set the JsonFile datasource
     *
     * @param jsonFile The JsonFile datasource
     * @since 1.0.0
     */
    public void setJsonFile(JsonFile jsonFile) {
        this.jsonFile = jsonFile;
    }

    /**
     * Get XmlFile datasource
     *
     * @return The JsonFile datasource
     * @since 1.0.0
     */
    public XmlFile getXmlFile() {
        return xmlFile;
    }

    /**
     * Set the XmlFile datasource
     *
     * @param xmlFile The JsonFile datasource
     * @since 1.0.0
     */
    public void setXmlFile(XmlFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * The pdf signature properties
     *
     * @return the properties
     * @since 1.0.0
     */
    public Sign getSign() {
        return sign;
    }

    /**
     * The pdf signature properties
     *
     * @param sign The properties
     * @since 1.0.0
     */
    public void setSign(Sign sign) {
        this.sign = sign;
        logger.debug("XmlHttp set to {}", this.sign == null ? "null" : this.sign.toString());
    }

    /**
     * Get the number of report copies
     *
     * @return The number of copies
     * @since 1.0.0
     */
    public int getCopies() {
        return copies;
    }

    /**
     * Set the number of report copies
     *
     * @param copies The number of copies
     * @since 1.0.0
     */
    public void setCopies(int copies) {
        this.copies = copies;
        logger.debug("Number of report copies set to {}", this.copies);
    }

    /**
     * Get the report encoding
     *
     * @return The encoding
     * @since 1.0.0
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * set the report encoding
     *
     * @param encoding The encoding
     * @since 1.0.0
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
        logger.debug("Report encoding set to {}", this.encoding);
    }

    /**
     * Get the report resources
     * @return The report resources
     * @since 1.0.0
     */
    public ArrayList<ReportResources> getReportResources() {
        return reportResources;
    }

    /**
     * Set the report resources
     * @param reportResources The report resources
     * @since 1.0.0
     */
    public void setReportResources(ArrayList<ReportResources> reportResources) {
        this.reportResources = reportResources;
    }

    /**
     * Get the bitwise after print operations
     *
     * @return The bitwise
     * @since 1.0.0
     */
    public int getAfterPrintOperations() {
        return afterPrintOperations;
    }

    /**
     * Set the bitwise after print operations
     *
     * @param afterPrintOperations The bitwise
     * @since 1.0.0
     */
    public void setAfterPrintOperations(int afterPrintOperations) {
        this.afterPrintOperations = afterPrintOperations;
    }

    /**
     * Document metadata
     * @return The metadata
     * @since 1.0.0
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Document metadata
     * @param metadata The metadata
     * @since 1.0.0
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the pdf properties
     * @return Properties
     * @since 1.0.0
     */
    public PdfProperties getPdfProperties() {
        return pdfProperties;
    }

    /**
     * Set The pdf properties
     * @param pdfProperties The properties
     * @since 1.0.0
     */
    public void setPdfProperties(PdfProperties pdfProperties) {
        this.pdfProperties = pdfProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportRequest request = (ReportRequest) o;
        return copies == request.copies
                && afterPrintOperations == request.afterPrintOperations
                && Objects.equals(report, request.report)
                && Objects.equals(reportResources, request.reportResources)
                && reportType == request.reportType
                && Objects.equals(parameters, request.parameters)
                && Objects.equals(database, request.database)
                && Objects.equals(jsonHttp, request.jsonHttp)
                && Objects.equals(jsonHttps, request.jsonHttps)
                && Objects.equals(xmlHttp, request.xmlHttp)
                && Objects.equals(xmlHttps, request.xmlHttps)
                && Objects.equals(jsonFile, request.jsonFile)
                && Objects.equals(xmlFile, request.xmlFile)
                && Objects.equals(sign, request.sign)
                && Objects.equals(encoding, request.encoding)
                && Objects.equals(metadata, request.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                report,
                reportResources,
                reportType,
                parameters,
                database,
                jsonHttp,
                jsonHttps,
                xmlHttp,
                xmlHttps,
                jsonFile,
                xmlFile,
                sign,
                copies,
                encoding,
                afterPrintOperations,
                metadata
        );
    }
}
