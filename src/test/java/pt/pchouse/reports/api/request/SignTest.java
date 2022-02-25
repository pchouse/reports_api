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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SignTest extends Generic<Sign> {

    @Test
    public void testSpecificAnnotations() {
        assertThat(
                Sign.class.getAnnotation(Scope.class).value().equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        ).isTrue();

        assertThat(
                Sign.class.getAnnotation(JsonIgnoreProperties.class).ignoreUnknown()
        ).isTrue();
    }

    @Test
    void testBean() {
        Sign sign = appContext.getBean(Sign.class);

        String keyStorePassword = "Key store password";
        String certificateName = "Certificate name";
        String certificatePassword = "Password";
        Sign.Level level = Sign.Level.CERTIFIED_NO_CHANGES_ALLOWED;
        SignRectangle signRectangle = new SignRectangle();
        String location = "Lisbon - Seoul";
        String reason = "Test application";
        String contact = "+351 999 999 999";

        sign.setKeyStorePassword(keyStorePassword);
        sign.setCertificateName(certificateName);
        sign.setCertificatePassword(certificatePassword);
        sign.setLevel(level);
        sign.setSignRectangle(signRectangle);
        sign.setVisible(false);
        sign.setLocation(location);
        sign.setReason(reason);
        sign.setContact(contact);

        assertThat(sign.getKeyStorePassword()).isEqualTo(keyStorePassword);
        assertThat(sign.getCertificateName()).isEqualTo(certificateName);
        assertThat(sign.getCertificatePassword()).isEqualTo(certificatePassword);
        assertThat(sign.getLevel()).isEqualTo(level);
        assertThat(sign.isVisible()).isEqualTo(false);
        assertThat(sign.getLocation()).isEqualTo(location);
        assertThat(sign.getReason()).isEqualTo(reason);
        assertThat(sign.getContact()).isEqualTo(contact);

        for (boolean bool : new boolean[]{true, false}) {
            sign.setVisible(bool);
            assertThat(sign.isVisible()).isEqualTo(bool);
        }

    }

    @Test
    void testToString() {
        assertThat((new Sign()).toString()).isNotEmpty();
    }

}
