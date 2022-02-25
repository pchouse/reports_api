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
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.reports.api.generator.ParseException;

import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PdfProperties {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Pdf user password
     * @since 1.0.0
     */
    private String userPassword;

    /**
     * Pdf owner password
     * @since 1.0.0
     */
    private String ownerPassword;

    /**
     * PDF javascript
     * @since 1.0.0
     */
    private String javascript;

    /**
     * Permissions. Bitwise defined in com.lowagie.text.pdf.PdfWriter
     * @since 1.0.0
     */
    private Integer permissions;

    /**
     * Pdf properties
     * @since 1.0.0
     */
    public PdfProperties() {
        logger.debug("New instance of {}", getClass().getName());
    }

    /**
     * Get user password
     * @return password
     * @since 1.0.0
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Set user password
     * @param userPassword password
     * @since 1.0.0
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        logger.debug(
                "User password set to '{}'", StringUtils.leftPad("*", this.userPassword.length(), "*")
        );
    }

    /**
     * Get the owner password
     * @return password
     * @since 1.0.0
     */
    public String getOwnerPassword() {
        return ownerPassword;
    }

    /**
     * Set owner password
     * @param ownerPassword password
     * @since 1.0.0
     */
    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
        logger.debug(
                "Owner password set to '{}'", StringUtils.leftPad("*", this.ownerPassword.length(), "*")
        );
    }

    /**
     * Get the javascript
     * @return javascript code
     * @since 1.0.0
     */
    public String getJavascript() {
        return javascript;
    }

    /**
     * Set the javascript
     * @param javascript javascript code
     * @since 1.0.0
     */
    public void setJavascript(String javascript) {
        this.javascript = javascript;
        logger.debug("Javascript set to: {}", this.javascript);
    }

    /**
     * Get permissions
     * @return Permissions bitwise
     * @since 1.0.0
     */
    public Integer getPermissions() {
        return permissions;
    }

    /**
     * Set the permissions<br>
     * Bitwise defined in com.lowagie.text.pdf.PdfWriter<br>
     * PdfWriter.ALLOW_ASSEMBLY<br>
     * PdfWriter.ALLOW_COPY<br>
     * PdfWriter.ALLOW_DEGRADED_PRINTING<br>
     * PdfWriter.ALLOW_FILL_IN<br>
     * PdfWriter.ALLOW_MODIFY_ANNOTATIONS<br>
     * PdfWriter.ALLOW_MODIFY_CONTENTS<br>
     * PdfWriter.ALLOW_PRINTING<br>
     * PdfWriter.ALLOW_SCREENREADERS;<br>
     * @param permissions Bitwise of permissions
     * @since 1.0.0
     */
    public void setPermissions(Integer permissions) throws ParseException {
        if(permissions.compareTo(0) == -1){
            String msg = "Permissions cannot be lower than zero";
            logger.error(msg);
            throw new ParseException(msg);
        }
        this.permissions = permissions;
        logger.debug("Permissions set to: {}", this.permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfProperties that = (PdfProperties) o;
        return Objects.equals(userPassword, that.userPassword)
                && Objects.equals(ownerPassword, that.ownerPassword)
                && Objects.equals(javascript, that.javascript)
                && Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPassword, ownerPassword, javascript, permissions);
    }

    public static final Integer getAllowAllPermissions() {
        return PdfWriter.ALLOW_ASSEMBLY
                | PdfWriter.ALLOW_COPY
                | PdfWriter.ALLOW_DEGRADED_PRINTING
                | PdfWriter.ALLOW_FILL_IN
                | PdfWriter.ALLOW_MODIFY_ANNOTATIONS
                | PdfWriter.ALLOW_MODIFY_CONTENTS
                | PdfWriter.ALLOW_PRINTING
                | PdfWriter.ALLOW_SCREENREADERS;
    }

}
