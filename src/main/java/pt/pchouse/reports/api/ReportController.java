/*
 * Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.pchouse.reports.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.pchouse.reports.api.auth.IAuth;
import pt.pchouse.reports.api.generator.IGenerator;
import pt.pchouse.reports.api.printer.Printer;
import pt.pchouse.reports.api.request.ReportRequest;
import pt.pchouse.reports.api.response.ReportResponse;
import pt.pchouse.reports.api.response.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The request controller
 * @since 1.0.0
 */
@RestController
public class ReportController {

    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @since 1.0.0
     */
    @Value("${pom.version}")
    private String pomVersion;

    /**
     * @since 1.0.0
     */
    @Autowired
    private ApplicationContext appContext;

    /**
     * @since 1.0.0
     */
    @Autowired
    private Executor executor;

    /**
     * Handler for report request
     * @param reportRequest The request
     * @return The response
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<ReportResponse>> getReport(@RequestBody ReportRequest reportRequest) {
        logger.debug("New report request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() ->
                {
                    ReportResponse reportResponse = appContext.getBean(ReportResponse.class);

                    try {
                        if (!auth.isAuthorized()) {
                            reportResponse.setStatus(ReportResponse.Status.ERROR);
                            reportResponse.setMessage("Client not authorized");
                            reportResponse.setDuration(startInstant, System.nanoTime());
                            logger.debug("Client not authorized, respond with http status code 400");
                            return ResponseEntity.status(400).body(reportResponse);
                        }

                        logger.debug("Going to generate the report");
                        IGenerator generator = appContext.getBean(IGenerator.class, reportRequest);

                        String generatorReport = generator.generate();
                        reportResponse.setReport(generatorReport);
                        reportResponse.setStatus(ReportResponse.Status.OK);
                        reportResponse.setDuration(startInstant, System.nanoTime());
                        logger.debug("Report generated elapsed time:" + reportResponse.getDuration());
                        return ResponseEntity.status(200).body(reportResponse);

                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        reportResponse.setStatus(ReportResponse.Status.ERROR);
                        reportResponse.setMessage(e.getMessage());
                        reportResponse.setDuration(startInstant, System.nanoTime());
                        return ResponseEntity.status(400).body(reportResponse);
                    }

                },
                executor
        );
    }

    /**
     * Handle to printer cut the paper
     * @return The response
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cut", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> cutPaper() {

        logger.debug("New cut paper request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (!auth.isAuthorized()) {
                    response.setStatus(ReportResponse.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                appContext.getBean(Printer.class).cutPaper();

                response.setStatus(ReportResponse.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Elapsed time:" + response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(ReportResponse.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handler to printer cut paper and open cash drawer
     * @return The response
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cutandopen", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> cutPaperAndOpenCashDrawer() {

        logger.debug("New cut paper and open cash drawer request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (!auth.isAuthorized()) {
                    response.setStatus(ReportResponse.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                appContext.getBean(Printer.class).cutAndCashDrawer();

                response.setStatus(ReportResponse.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Elapsed time:" + response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(ReportResponse.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handle to printer open cash drawer
     * @return The response
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cashdrawer", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> openCashDrawerPaper() {

        logger.debug("New open cash drawer request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (!auth.isAuthorized()) {
                    response.setStatus(ReportResponse.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                appContext.getBean(Printer.class).cashDrawer();

                response.setStatus(ReportResponse.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Elapsed time:" + response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(ReportResponse.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Get version
     * @return The response
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> getVersion() {

        logger.debug("Get version");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (!auth.isAuthorized()) {
                    response.setStatus(ReportResponse.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                response.setStatus(ReportResponse.Status.OK);
                response.setMessage(pomVersion);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Elapsed time:" + response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(ReportResponse.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Catch all non-existent action path
     *
     * @return HttpStatus 404 with message "Action not found"
     * @since 1.0.0
     */
    @RequestMapping("*")
    public CompletableFuture<ResponseEntity<ReportResponse>> catchAllNotFound() {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug("Catch capture a request for a method that not exist");
            ReportResponse reportResponse = appContext.getBean(ReportResponse.class);
            reportResponse.setStatus(ReportResponse.Status.ERROR);
            reportResponse.setMessage("Action not found");
            reportResponse.setDuration(null);
            return ResponseEntity.status(404).body(reportResponse);
        });
    }

}
