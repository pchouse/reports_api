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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParameterTest extends Generic<Parameter> {

    @Test
    @Override
    public void testAnnotations() {
        assertThat(Parameter.class.isAnnotationPresent(Component.class)).isTrue();
        assertThat(Parameter.class.isAnnotationPresent(Scope.class)).isTrue();
        assertThat(
                Parameter.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();
    }

    @Test
    void testBean() {
        String name = "Name: ";
        String format = "YYYY-MM-DD";
        String value = "Value: ";

        for (Parameter.Types type : Parameter.Types.values()) {

            Parameter parameter = appContext.getBean(Parameter.class);

            parameter.setType(type);
            parameter.setName(name + type);
            parameter.setFormat(format);
            parameter.setValue(value + type);

            assertThat(parameter.getType()).isEqualTo(type);
            assertThat(parameter.getName()).isEqualTo(name + type);
            assertThat(parameter.getFormat()).isEqualTo(format);
            assertThat(parameter.getValue()).isEqualTo(value + type);
        }

    }

    @Test
    void testBooleanTrue() {

        for (Parameter.Types type : new Parameter.Types[]{
                Parameter.Types.P_BOOLEAN, Parameter.Types.P_BOOL}) {

            for (String value : new String[]{"1", "yes", "Yes", "on", "On"}) {
                Parameter parameter = appContext.getBean(Parameter.class);
                parameter.setType(type);
                parameter.setValue(value);

                assertThat(parameter.getValue()).isEqualTo("true");

            }

        }
    }

}
