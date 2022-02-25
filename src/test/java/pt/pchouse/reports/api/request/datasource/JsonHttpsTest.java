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

package pt.pchouse.reports.api.request.datasource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JsonHttpsTest extends Generic<JsonHttps> {

    @Test
    public void testSpecificAnnotations() {
        assertThat(
                JsonHttps.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();

        assertThat(
                JsonHttps.class.getAnnotation(JsonIgnoreProperties.class).ignoreUnknown()
        ).isTrue();
    }

    @Test
    void testBean() {

        String datePattern = "Date pattern";
        String numberPatter = "Number pattern";
        String url = "https://localhost";

        for (HttpType httpType : HttpType.values()) {

            JsonHttps jsonHttps = appContext.getBean(JsonHttps.class);

            jsonHttps.setType(httpType);
            jsonHttps.setDatePattern(datePattern);
            jsonHttps.setNumberPattern(numberPatter);
            jsonHttps.setUrl(url);

            assertThat(jsonHttps.getType()).isEqualTo(httpType);
            assertThat(jsonHttps.getDatePattern()).isEqualTo(datePattern);
            assertThat(jsonHttps.getNumberPattern()).isEqualTo(numberPatter);
            assertThat(jsonHttps.getUrl()).isEqualTo(url);

        }

    }

}
