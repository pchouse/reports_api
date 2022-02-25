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
import pt.pchouse.reports.Generic;

import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
public class PrinterTest extends Generic<Printer> {

    @Override
    @Test
    public void testAnnotations() {
        assert true;
    }

    @Test
    void testCutPaper() {
        try {
            appContext.getBean(Printer.class).cutPaper();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCashDrawer() {
        try {
            appContext.getBean(Printer.class).cashDrawer();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCutAndCashDrawer() {
        try {
            appContext.getBean(Printer.class).cutAndCashDrawer();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
