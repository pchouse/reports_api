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

package pt.pchouse.reports.api.generator;

import com.itextpdf.text.Rectangle;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import rebelo.reports.core.NullNotAllowedException;
import rebelo.reports.core.RRProperties;
import rebelo.reports.core.Report;
import rebelo.reports.core.datasource.*;
import rebelo.reports.core.sign.RRSignPdfProperties;

/**
 * Factories for DI
 * @since 1.0.0
 */
@Configuration
public class Factory {

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRProperties rrPropertiesFactory() {
        return new RRProperties();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsDatabase rrDsDatabaseFactory() {
        return new RRDsDatabase();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsHttpsJson rrDsHttpsJsonFactory() {
        return new RRDsHttpsJson();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsHttpJson rrDsHttpJsonFactory() {
        return new RRDsHttpJson();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsHttpsXml rrDsHttpsXmlFactory() {
        return new RRDsHttpsXml();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsHttpXml rrDsHttpXmlFactory() {
        return new RRDsHttpXml();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsFileJson rrDsFileJsonFactory() {
        return new RRDsFileJson();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRDsFileXml rrDsFileXmlFactory() {
        return new RRDsFileXml();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public RRSignPdfProperties rrSignPdfPropertiesFactory() {
        return new RRSignPdfProperties();
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public Rectangle rectangleFactory(float upperX, float upperY, int rotation) {
        return new Rectangle(upperX, upperY, rotation);
    }

    /**
     *
     * @since 1.0.0
     * @return The instance
     */
    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public Report reportFactory(RRProperties properties) throws NullNotAllowedException {
        return new Report(properties);
    }

}
