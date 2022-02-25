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
public class SignRectangle {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Axial coordinate (x)
     * @since 1.0.0
     */
    private int x;

    /**
     * Ordinate coordinate (y)
     * @since 1.0.0
     */
    private int y;

    /**
     * The rectangle width
     * @since 1.0.0
     */
    private int width;

    /**
     * The rectangle height
     * @since 1.0.0
     */
    private int height;

    /**
     * The rectangle rotation: 0 , 90 , 180 or 270
     * @since 1.0.0
     */
    private int rotation;

    /**
     * Position of the rectangle image signature (Java Rectangle class coordinates)
     * @since 1.0.0
     */
    public SignRectangle() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Axial coordinate (x)
     *
     * @return The coordinate
     * @since 1.0.0
     */
    public int getX() {
        return x;
    }

    /**
     * Axial coordinate (x)
     *
     * @param x The coordinate
     * @since 1.0.0
     */
    public void setX(int x) {
        this.x = x;
        logger.debug("Axial set to {}", this.x);
    }

    /**
     * Ordinate coordinate (y)
     *
     * @return The coordinate
     * @since 1.0.0
     */
    public int getY() {
        return y;
    }

    /**
     * Ordinate coordinate (y)
     *
     * @param y The coordinate
     * @since 1.0.0
     */
    public void setY(int y) {
        this.y = y;
        logger.debug("Ordinate set to {}", this.y);
    }

    /**
     * The rectangle width
     *
     * @return The width
     * @since 1.0.0
     */
    public int getWidth() {
        return width;
    }

    /**
     * The rectangle width
     *
     * @param width The width
     * @since 1.0.0
     */
    public void setWidth(int width) {
        this.width = width;
        logger.debug("Width set to {}", this.width);
    }

    /**
     * The rectangle height
     *
     * @return The height
     * @since 1.0.0
     */
    public int getHeight() {
        return height;
    }

    /**
     * The rectangle height
     *
     * @param height The height
     * @since 1.0.0
     */
    public void setHeight(int height) {
        this.height = height;
        logger.debug("Height set to {}", this.height);
    }

    /**
     * The rectangle rotation: 0 , 90 , 180 or 270
     *
     * @return the angle
     * @since 1.0.0
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * The rectangle rotation: 0 , 90 , 180 or 270
     *
     * @param rotation The angle
     * @since 1.0.0
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
        logger.debug("Rotation set to {}", this.rotation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignRectangle that = (SignRectangle) o;
        return x == that.x && y == that.y && width == that.width
                && height == that.height && rotation == that.rotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, rotation);
    }
}
