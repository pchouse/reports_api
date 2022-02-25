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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @since 1.0.0
 */
public abstract class APattern {

    /**
     *
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Date pattern in the datasource values
     * @since 1.0.0
     */
    private String datePattern;

    /**
     * The number pattern in the datasource values
     * @since 1.0.0
     */
    private String numberPattern;


    /**
     * The Date pattern in the datasource values
     *
     * @return The pattern
     * @since 1.0.0
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * The Date pattern in the datasource values
     *
     * @param datePattern The pattern
     * @since 1.0.0
     */
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        logger.debug(
                "DatePattern set to {}",
                this.datePattern == null ? "null" : this.datePattern
        );
    }

    /**
     * The number pattern in the datasource values
     *
     * @return The pattern
     * @since 1.0.0
     */
    public String getNumberPattern() {
        return numberPattern;
    }

    /**
     * The number pattern in the datasource values
     *
     * @param numberPattern The pattern
     * @since 1.0.0
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
        logger.debug(
                "NumberPattern set to {}",
                this.numberPattern == null ? "null" : this.numberPattern
        );
    }
}
