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

package pt.pchouse.reports.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pt.pchouse.reports.api.response.ReportResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @since 1.0.0
 */
@ControllerAdvice
public class ErrorHandler {

    /**
     * @since 1.0.0
     */
    @Autowired
    private ApplicationContext appContext;

    /**
     * @param ex       The not handled exception
     * @param request  The request container
     * @param response The response container
     * @return The Internal Error response
     * @see <a href="https://stackoverflow.com/a/48509042">https://stackoverflow.com/a/48509042</a>
     * @since 1.0.0
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ReportResponse> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ReportResponse reportResponse = appContext.getBean(ReportResponse.class);
        reportResponse.setStatus(ReportResponse.Status.ERROR);

        if (ex instanceof HttpMessageNotReadableException) {
            reportResponse.setMessage("Request body error. Possible empty or json wrong format.");
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            reportResponse.setMessage("Request body wrong type.");
        } else {
            reportResponse.setMessage(ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(reportResponse);
    }

}
