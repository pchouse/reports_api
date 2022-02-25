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

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.reports.api.request.ReportRequest;

/**
 *
 * @since 1.0.0
 */
@Service
@RequestScope
public interface IGenerator {

    /**
     * Generate the report and return as base64 encoded string
     *
     * @return The generated report as base64 string
     * @since 1.0.0
     */
    String generate() throws Exception;

    /**
     * Get the ReportRequest passed in the constructor
     *
     * @return The ReportRequest
     * @since 1.0.0
     */
    ReportRequest getReportRequest();

}
