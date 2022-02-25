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

import com.lowagie.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.reports.Generic;
import pt.pchouse.reports.api.generator.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class PdfPropertiesTest extends Generic<PdfProperties> {

    @Test
    @Override
    public void testAnnotations() {
        assertThat(PdfProperties.class.isAnnotationPresent(Component.class)).isTrue();
        assertThat(PdfProperties.class.isAnnotationPresent(Scope.class)).isTrue();
        assertThat(
                PdfProperties.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();
    }

    @Test
    void testBean() throws ParseException {

        PdfProperties properties = appContext.getBean(PdfProperties.class);

        String userPassword = "The user password";
        String ownerPassword = "The owner password";
        String javaScript = "Tha javascript code";
        Integer permisstions = 999;

        properties.setUserPassword(userPassword);
        properties.setOwnerPassword(ownerPassword);
        properties.setJavascript(javaScript);
        properties.setPermissions(permisstions);

        assertThat(properties.getUserPassword()).isEqualTo(userPassword);
        assertThat(properties.getOwnerPassword()).isEqualTo(ownerPassword);
        assertThat(properties.getJavascript()).isEqualTo(javaScript);
        assertThat(properties.getPermissions()).isEqualTo(permisstions);

        properties.setPermissions(0);
        assertThat(properties.getPermissions()).isEqualTo(0);

        assertThat(properties.getAllowAllPermissions()).isEqualTo(
                PdfWriter.ALLOW_ASSEMBLY
                        | PdfWriter.ALLOW_COPY
                        | PdfWriter.ALLOW_DEGRADED_PRINTING
                        | PdfWriter.ALLOW_FILL_IN
                        | PdfWriter.ALLOW_MODIFY_ANNOTATIONS
                        | PdfWriter.ALLOW_MODIFY_CONTENTS
                        | PdfWriter.ALLOW_PRINTING
                        | PdfWriter.ALLOW_SCREENREADERS
        );
    }

    @Test
    void testSetPermissionsLowerThanZero(){
        PdfProperties pdfProperties = new PdfProperties();
        try{
            pdfProperties.setPermissions(-1);
            fail("Should throw ParseException when set to a negative number");
        }catch (ParseException e){
            assertThat(e.getMessage()).isEqualTo("Permissions cannot be lower than zero");
        }
    }

}
