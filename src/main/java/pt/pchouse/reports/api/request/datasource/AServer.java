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
public abstract class AServer extends APattern {

    /**
     *
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The server URL
     * @since 1.0.0
     */
    private String url;

    /**
     * The HTTP request verb, POST or GET
     * @since 1.0.0
     */
    private HttpType type;

    /**
     *
     * @since 1.0.0
     */
    public AServer() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * The server URL
     *
     * @return The URL as string
     * @since 1.0.0
     */
    public String getUrl() {
        return url;
    }

    /**
     * The server URL
     *
     * @param url The URL as string
     * @since 1.0.0
     */
    public void setUrl(String url) {
        this.url = url;
        logger.debug(
                "URL set to {}",
                this.url == null ? "null" : this.url
        );
    }

    /**
     * The HTTP request verb, POST or GET
     *
     * @return The request verb
     * @since 1.0.0
     */
    public HttpType getType() {
        return type;
    }

    /**
     * The HTTP request verb, POST or GET
     *
     * @param type The request verb
     * @since 1.0.0
     */
    public void setType(HttpType type) {
        this.type = type;
        logger.debug(
                "Type set to {}",
                this.type == null ? "null" : this.type
        );
    }
}
