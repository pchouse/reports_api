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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.print.*;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class Printer {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @since 1.0.0
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     *
     * @since 1.0.0
     */
    @Autowired
    private PrinterConfig printerConfig;

    /**
     *
     * @since 1.0.0
     */
    private static PrintService printService;

    /**
     *
     * @return The printer service
     * @throws PrinterException If error
     * @since 1.0.0
     */
    public PrintService getPrintService() throws PrinterException {
        if (printService == null) {

            Optional<PrintService> optional = Arrays.stream(PrintServiceLookup
                    .lookupPrintServices(null, null))
                    .filter(prtService -> prtService.getName().equals(printerConfig.getName()))
                    .findFirst();

            if (!optional.isPresent()) {
                String msg = String.format(
                        "Printer with name '%s' not exist, if exists please restart the APi Reports service",
                        printerConfig.getName()
                );
                logger.error(msg);
                throw new PrinterException(msg);
            }
            logger.debug("Printer service set to printer name '{}'", printerConfig.getName());
            printService = optional.get();
        }

        return printService;
    }

    /**
     * Cut the paper
     *
     * @throws PrintException   If fails
     * @throws PrinterException If fails
     * @since 1.0.0
     */
    public void cutPaper() throws PrintException, PrinterException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCutBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );
        getPrintService().createPrintJob().print(doc, null);
    }

    /**
     * Open the cash drawer
     *
     * @throws PrintException   If fails
     * @throws PrinterException If fails
     * @since 1.0.0
     */
    public void cashDrawer() throws PrintException, PrinterException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCashDrawerBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );
        getPrintService().createPrintJob().print(doc, null);
    }

    /**
     * Cut the paper and open cash drawer
     *
     * @throws PrintException   If fails
     * @throws PrinterException If fails
     * @since 1.0.0
     */
    public void cutAndCashDrawer() throws PrintException, PrinterException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCutBytes());
        esc += new String(printerConfig.getCashDrawerBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );
        getPrintService().createPrintJob().print(doc, null);
    }

}
