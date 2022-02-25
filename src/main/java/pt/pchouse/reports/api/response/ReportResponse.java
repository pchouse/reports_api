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

package pt.pchouse.reports.api.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class ReportResponse extends AResponse {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The response in base64 encode
     * @since 1.0.0
     */
    private String report;

    /**
     * The Report Response
     * @since 1.0.0
     */
    public ReportResponse() {
        super();
    }

    /**
     * Get the generated Report as base64 encoded
     *
     * @return The generated report
     * @since 1.0.0
     */
    public String getReport() {
        return report;
    }

    /**
     * Set the generated report as base64 encoded
     *
     * @param report The generated report
     * @since 1.0.0
     */
    public void setReport(String report) {
        this.report = report;
        logger.debug("Report was set");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportResponse that = (ReportResponse) o;
        return status == that.status
                && Objects.equals(message, that.message)
                && Objects.equals(report, that.report);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, report);
    }

}
