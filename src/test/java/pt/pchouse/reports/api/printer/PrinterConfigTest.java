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

package pt.pchouse.reports.api.printer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PrinterConfigTest extends Generic<PrinterConfig> {

    @Override
    @Test
    public void testAnnotations() {
        assertThat(PrinterConfig.class.isAnnotationPresent(Configuration.class)).isTrue();
    }

    byte[] convert(String toConvert) {
        String[] split = toConvert.split(PrinterConfig.SEPARATOR);
        byte[] bytes = new byte[split.length];
        int index = 0;
        for (String code : split) {
            bytes[index++] = Byte.parseByte(code);
        }
        return bytes;
    }

    @Test
    void testBean() {
        String name = "The printer name";
        String init = "27,10";
        String feed = "99";
        String cut = "10,20";
        String cashDrawer = "20,30,40";

        PrinterConfig printerConfig = new PrinterConfig();
        printerConfig.setName(name);
        printerConfig.setInit(init);
        printerConfig.setFeed(feed);
        printerConfig.setCut(cut);
        printerConfig.setCashDrawer(cashDrawer);

        assertThat(printerConfig.getName()).isEqualTo(name);
        assertThat(printerConfig.getInit()).isEqualTo(init);
        assertThat(printerConfig.getFeed()).isEqualTo(feed);
        assertThat(printerConfig.getCut()).isEqualTo(cut);
        assertThat(printerConfig.getCashDrawer()).isEqualTo(cashDrawer);

        assertThat(printerConfig.getInitBytes()).isEqualTo(convert(init));
        assertThat(printerConfig.getFeedBytes()).isEqualTo(convert(feed));
        assertThat(printerConfig.getCutBytes()).isEqualTo(convert(cut));
        assertThat(printerConfig.getCashDrawerBytes()).isEqualTo(convert(cashDrawer));

    }

    @Test
    void testDefaultConfig() {
        PrinterConfig printerConfig = appContext.getBean(PrinterConfig.class);
        assertThat(printerConfig.getName()).isNotEmpty();
        assertThat(printerConfig.getInit()).isNotEmpty();
        assertThat(printerConfig.getFeed()).isNotEmpty();
        assertThat(printerConfig.getCut()).isNotEmpty();
        assertThat(printerConfig.getCashDrawer()).isNotEmpty();
    }


}
