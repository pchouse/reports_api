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

package pt.pchouse.reports.api.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pt.pchouse.reports.Generic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthTest extends Generic<Auth> {

    @Test
    void testGetRemoteIp() {
        Auth auth = appContext.getBean(Auth.class);
        auth.catchRemoteIP();
        String ips = auth.getRemoteIp();
        assertThat(ips).isEqualTo("127.0.0.1");
    }

    @Test
    void testGetAllowIps() throws IOException {
        Properties properties = new Properties();
        properties.load(
                getClass().getClassLoader().getResourceAsStream("application.properties")
        );

        String propAllowIps = properties.getProperty("client.allowIps");
        Auth auth = appContext.getBean(Auth.class);

        assertThat(auth.getAllowIps()).isEqualTo(propAllowIps.split(","));

    }

    @Test
    void testIsRemoteIpAllowedAllowIpNullOrEmpty() throws NoSuchFieldException, IllegalAccessException {
        Auth auth = new Auth();
        Field field = auth.getClass().getDeclaredField("allowIps");
        field.setAccessible(true);

        field.set(auth, null);
        assertThat(auth.isRemoteIpAllowed()).isTrue();

        field.set(auth, new String[]{});
        assertThat(auth.isRemoteIpAllowed()).isTrue();

    }

    @Test
    void testIsRemoteIpAllowedAllowIPLocalHost() throws NoSuchFieldException, IllegalAccessException {
        Auth auth = new Auth();
        auth.catchRemoteIP();
        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);

        fieldAllowIps.set(auth, new String[]{"localhost"});
        assertThat(auth.isRemoteIpAllowed()).isTrue();

        for (String remoteIP : new String[]{"localhost", "127.0.0.1", "::1", "0:0:0:0:0:0:0:1"}) {
            fieldRemoteIp.set(auth, remoteIP);
            assertThat(auth.getRemoteIp()).isEqualTo(remoteIP);
            assertThat(auth.isRemoteIpAllowed()).isTrue();
        }

    }

    @Test
    void testIsRemoteIpAllowedAllowIPWrongIp() throws NoSuchFieldException, IllegalAccessException {
        Auth auth = new Auth();
        auth.catchRemoteIP();

        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);

        fieldAllowIps.set(auth, new String[]{"192.168.1.9"});
        assertThat(auth.isRemoteIpAllowed()).isFalse();
    }

    @Test
    void testIsRemoteIpAllowedAllowIPSingleIp() throws NoSuchFieldException, IllegalAccessException {
        String remoteIp = "192.168.1.9";
        Auth auth = new Auth();

        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);
        fieldAllowIps.set(auth, new String[]{remoteIp});

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);
        fieldRemoteIp.set(auth, remoteIp);

        assertThat(auth.isRemoteIpAllowed()).isTrue();
    }

    @Test
    void testIsRemoteIpAllowedAllowIPStackIp() throws NoSuchFieldException, IllegalAccessException {
        String remoteIp = "192.168.1.9";
        Auth auth = new Auth();

        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);
        fieldAllowIps.set(auth, new String[]{"192.168.9.49", remoteIp, "10.0.0.1"});

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);
        fieldRemoteIp.set(auth, remoteIp);

        assertThat(auth.isRemoteIpAllowed()).isTrue();
    }

    @Test
    void testIsAuthorized() throws NoSuchFieldException, IllegalAccessException {
        String remoteIp = "192.168.1.9";
        Auth auth = new Auth();

        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);
        fieldAllowIps.set(auth, new String[]{"192.168.9.49", remoteIp, "10.0.0.1"});

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);
        fieldRemoteIp.set(auth, remoteIp);

        assertThat(auth.isAuthorized()).isTrue();
    }

    @Test
    void testIsAuthorizedWrongIp() throws NoSuchFieldException, IllegalAccessException {
        String remoteIp = "192.168.1.9";
        Auth auth = new Auth();

        Field fieldAllowIps = auth.getClass().getDeclaredField("allowIps");
        fieldAllowIps.setAccessible(true);
        fieldAllowIps.set(auth, new String[]{"192.168.9.49", "10.0.0.1", "localhost"});

        Field fieldRemoteIp = auth.getClass().getDeclaredField("remoteIp");
        fieldRemoteIp.setAccessible(true);
        fieldRemoteIp.set(auth, remoteIp);

        assertThat(auth.isAuthorized()).isFalse();
    }


}
