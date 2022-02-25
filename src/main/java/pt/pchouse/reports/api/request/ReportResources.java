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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ReportResources {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The resource file name
     * @since 1.0.0
     */
    private String name;

    /**
     * The base64  encoded resource file as string.
     * @since 1.0.0
     */
    private String resource;

    /**
     * Can be used to any files that the report must load, like sub reports, images.
     * It will be placed in the same directory as the report.jasper
     * @since 1.0.0
     */
    public ReportResources() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the resource file name
     *
     * @return The report name
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Set the resource file name
     *
     * @param name The report name
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the base64 encoded resource file as string
     *
     * @return The base64 report string
     * @since 1.0.0
     */
    public String getResource() {
        return resource;
    }

    /**
     * Set base64 encoded resource file as string
     *
     * @param resource The base64 report string
     * @since 1.0.0
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportResources reportResources = (ReportResources) o;
        return Objects.equals(name, reportResources.name) && Objects.equals(resource, reportResources.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resource);
    }
}
