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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReportResponseTest extends Generic<ReportResponse> {

    @Test
    public void testSpecificAnnotations() {
        assertThat(
                ReportResponse.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();

    }

    @Test
    void testBean() {
        ReportResponse reportResponse = appContext.getBean(ReportResponse.class);

        ReportResponse.Status status = ReportResponse.Status.OK;
        String message = "The error message";
        String report = "A base64 encoded string";
        String duration = "1s";

        reportResponse.setStatus(status);
        reportResponse.setMessage(message);
        reportResponse.setReport(report);
        reportResponse.setDuration(duration);

        assertThat(reportResponse.getStatus()).isEqualTo(status);
        assertThat(reportResponse.getMessage()).isEqualTo(message);
        assertThat(reportResponse.getReport()).isEqualTo(report);
        assertThat(reportResponse.getDuration()).isEqualTo(duration);

    }

    @Test
    void testSetDurationFromStartAndEndTime() {
        ReportResponse response = new ReportResponse();
        long now = System.nanoTime();
        long nanoPow = (long) Math.pow(10, 9);
        response.setDuration(now, now + nanoPow);
        assertThat(response.getDuration()).isEqualTo("1s.0");

        response.setDuration(now, now + nanoPow / 2);
        assertThat(response.getDuration()).isEqualTo("0s.500000000");

        response.setDuration(now, now + nanoPow * 2);
        assertThat(response.getDuration()).isEqualTo("2s.0");
    }

}
