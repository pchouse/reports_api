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
public class XmlFile extends APattern {

    /**
     *
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The base64 xml encoded
     * @since 1.0.0
     */
    private String xml;

    /**
     *
     * @since 1.0.0
     */
    public XmlFile() {
        logger.debug("Start new instance of {}", this.getClass().getName());
    }

    /**
     * Get the xml datasource. A xml base64 encoded string
     *
     * @return The base64 xml encoded string
     * @since 1.0.0
     */
    public String getXml() {
        return xml;
    }

    /**
     * Set the xml datasource. A xml base64 encoded string
     *
     * @param xml The base64 xml encoded string
     * @since 1.0.0
     */
    public void setXml(String xml) {
        this.xml = xml;
        logger.debug("Xml base64 string was set");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XmlFile xmlFile = (XmlFile) o;

        return Objects.equals(xml, xmlFile.xml)
                && Objects.equals(getDatePattern(), xmlFile.getDatePattern())
                && Objects.equals(getNumberPattern(), xmlFile.getNumberPattern());
    }

    @Override
    public int hashCode() {
        return xml.hashCode();
    }
}
