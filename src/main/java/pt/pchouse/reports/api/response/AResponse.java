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

package pt.pchouse.reports.api.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 *
 * @since 1.0.0
 */
abstract public class AResponse {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The response status enumeration
     * @since 1.0.0
     */
    public enum Status {
        OK,
        ERROR,
    }

    /**
     * The report status response
     * @since 1.0.0
     */
    protected Status status;

    /**
     * Response message
     * @since 1.0.0
     */
    protected String message = "";

    /**
     * The Execution duration human readable format
     * @since 1.0.0
     */
    protected String duration;

    /**
     * The request Response
     * @since 1.0.0
     */
    public AResponse() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the response report status
     *
     * @return The status
     * @since 1.0.0
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the response report status
     *
     * @param status the status
     * @since 1.0.0
     */
    public void setStatus(Status status) {
        this.status = status;
        logger.debug("Response status set to {}", this.status.toString());
    }

    /**
     * Get the response message
     *
     * @return The message
     * @since 1.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the response message
     *
     * @param message The message
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
        logger.debug("Message set to {}", this.message);
    }

    /**
     * The Execution duration human readable format
     *
     * @return The duration phrase
     * @since 1.0.0
     */
    public String getDuration() {
        return duration;
    }

    /**
     * The Execution duration human readable format
     *
     * @param duration The duration phrase
     * @since 1.0.0
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Set the human readable duration time from the start and end nano time
     *
     * @param start The start nano time
     * @param end   The end nano time
     * @since 1.0.0
     */
    public void setDuration(long start, long end) {
        Duration duration = Duration.ofNanos(end - start);
        this.duration = String.format("%ds.%s", duration.getSeconds(), duration.getNano());
    }

}
