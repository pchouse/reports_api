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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @since 1.0.0
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:defaultprinter.properties"),
        @PropertySource(value = "file:${app.home}/printer.properties", ignoreResourceNotFound = true)
})
public class PrinterConfig {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The ESC/POS byte separator int the config file
     * @since 1.0.0
     */
    public final static String SEPARATOR = ",";

    /**
     * The printer name
     * @since 1.0.0
     */
    @Value("${name}")
    private String name;

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     * @since 1.0.0
     */
    @Value("${init}")
    private String init;

    /**
     * The printer initialization ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _init;

    /**
     * The ESC/POS command from configuration file
     * To line feed
     * @since 1.0.0
     */
    @Value("${feed}")
    private String feed;

    /**
     * The line feed ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _feed;

    /**
     * The ESC/POS command from configuration file
     * To cut paper
     * @since 1.0.0
     */
    @Value("${cut}")
    private String cut;

    /**
     * The cut paper ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _cut;

    /**
     * The ESC/POS command from configuration file
     * To cash drawer
     * @since 1.0.0
     */
    @Value("${cash_drawer}")
    private String cashDrawer;

    /**
     * The cash drawer ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _cashDrawer;

    /**
     * Printer configuration
     * @since 1.0.0
     */
    public PrinterConfig() {
        logger.debug("New instance of {}", getClass().getName());
    }

    /**
     * Get the get printer name
     *
     * @return The printer name
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Set the printer name
     *
     * @param name The printer name
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    public String getInit() {
        return init;
    }

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     *
     * @param init The codes separated by a comma
     * @since 1.0.0
     */
    public void setInit(String init) {
        this.init = init;
    }

    /**
     * The ESC/POS command from configuration file
     * To line feed
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    public String getFeed() {
        return feed;
    }

    /**
     * The ESC/POS command from configuration file
     * To line feed
     *
     * @param feed The codes separated by a comma
     * @since 1.0.0
     */
    public void setFeed(String feed) {
        this.feed = feed;
    }

    /**
     * The ESC/POS command from configuration file
     * To to cut the paper
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    public String getCut() {
        return cut;
    }

    /**
     * The ESC/POS command from configuration file
     * To cut the paper
     *
     * @param cut The codes separated by a comma
     * @since 1.0.0
     */
    public void setCut(String cut) {
        this.cut = cut;
    }

    /**
     * The ESC/POS command from configuration file
     * To open cash drawer
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    public String getCashDrawer() {
        return cashDrawer;
    }

    /**
     * The ESC/POS command from configuration file
     * To to open the cash drawer
     *
     * @param cashDrawer The codes separated by a comma
     * @since 1.0.0
     */
    public void setCashDrawer(String cashDrawer) {
        this.cashDrawer = cashDrawer;
    }

    /**
     * The printer initialization ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getInitBytes() {
        if (_init == null) {
            String[] initSplit = init.split(SEPARATOR);
            _init = new byte[initSplit.length];
            int index = 0;
            for (String code : initSplit) {
                _init[index++] = Byte.parseByte(code);
            }
        }
        return _init;
    }

    /**
     * The line feed ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getFeedBytes() {
        if (_feed == null) {
            String[] feedSplit = feed.split(SEPARATOR);
            _feed = new byte[feedSplit.length];
            int index = 0;
            for (String code : feedSplit) {
                _feed[index++] = Byte.parseByte(code);
            }
        }
        return _feed;
    }

    /**
     * The paper cut ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getCutBytes() {
        if (_cut == null) {
            String[] cutSplit = cut.split(SEPARATOR);
            _cut = new byte[cutSplit.length];
            int index = 0;
            for (String code : cutSplit) {
                _cut[index++] = Byte.parseByte(code);
            }
        }
        return _cut;
    }

    /**
     * The cash drawer ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getCashDrawerBytes() {
        if (_cashDrawer == null) {
            String[] cashDrawerSplit = cashDrawer.split(SEPARATOR);
            _cashDrawer = new byte[cashDrawerSplit.length];
            int index = 0;
            for (String code : cashDrawerSplit) {
                _cashDrawer[index++] = Byte.parseByte(code);
            }
        }
        return _cashDrawer;
    }

}
