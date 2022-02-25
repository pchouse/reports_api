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
public class JsonFile extends APattern {

    /**
     *
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The base64 json encoded
     * @since 1.0.0
     */
    private String json;

    /**
     *
     * @since 1.0.0
     */
    public JsonFile() {
        logger.debug("Start new instance of {}", this.getClass().getName());
    }

    /**
     * Get the json datasource. A json base64 encoded string
     *
     * @return The base64 json encoded string
     * @since 1.0.0
     */
    public String getJson() {
        return json;
    }

    /**
     * Set the json datasource. A json base64 encoded string
     *
     * @param json The base64 json encoded string
     * @since 1.0.0
     */
    public void setJson(String json) {
        this.json = json;
        logger.debug("Json base64 string was set");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonFile jsonFile = (JsonFile) o;

        return Objects.equals(json, jsonFile.json)
                && Objects.equals(getDatePattern(), jsonFile.getDatePattern())
                && Objects.equals(getNumberPattern(), jsonFile.getNumberPattern());
    }

    @Override
    public int hashCode() {
        return json.hashCode();
    }

}
