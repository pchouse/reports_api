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
public class Sign {

    /**
     * The signature certification level options
     * @since 1.0.0
     */
    public enum Level {
        CERTIFIED_NO_CHANGES_ALLOWED,
        CERTIFIED_FORM_FILLING,
        CERTIFIED_FORM_FILLING_AND_ANNOTATIONS
    }

    /**
     * The type of certificate option
     * @since 1.0.0
     */
    public enum CertificateType {
        SELF
    }

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The java key store password
     * @since 1.0.0
     */
    private String keyStorePassword;

    /**
     * The certificate name (alias) in the keystore
     * @since 1.0.0
     */
    private String certificateName;

    /**
     * The certificate password
     * @since 1.0.0
     */
    private String certificatePassword;

    /**
     * The signature certification level
     * @since 1.0.0
     */
    private Level level;

    /**
     * The type of certificate
     * @since 1.0.0
     */
    private CertificateType certificateType;

    /**
     * The visibility and position of the signature image rectangle
     * @since 1.0.0
     */
    private SignRectangle signRectangle;

    /**
     * Define if the signature is visible or not
     * @since 1.0.0
     */
    private boolean visible;

    /**
     * The legend 'Location' to be write in the signature
     * @since 1.0.0
     */
    private String location;

    /**
     * The legend 'Reason' to be write in the signature
     * @since 1.0.0
     */
    private String reason;

    /**
     * A contact
     * @since 1.0.0
     */
    private String contact;

    /**
     * The pdf digital signature properties
     * @since 1.0.0
     */
    public Sign() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * The java key store password
     *
     * @return The password
     * @since 1.0.0
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * The java key store password
     *
     * @param keyStorePassword The password
     * @since 1.0.0
     */
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    /**
     * The certificate name (alias) in the keystore
     *
     * @return The name
     * @since 1.0.0
     */
    public String getCertificateName() {
        return certificateName;
    }

    /**
     * The certificate name (alias) in the keystore
     *
     * @param certificateName The name
     * @since 1.0.0
     */
    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    /**
     * The certificate password
     *
     * @return The password
     * @since 1.0.0
     */
    public String getCertificatePassword() {
        return certificatePassword;
    }

    /**
     * The certificate password
     *
     * @param certificatePassword The certificate password
     * @since 1.0.0
     */
    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    /**
     * The signature certification level
     *
     * @return The level
     * @since 1.0.0
     */
    public Level getLevel() {
        return level;
    }

    /**
     * The signature certification level
     *
     * @param level The level
     * @since 1.0.0
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * The type of certificate
     *
     * @return The type
     * @since 1.0.0
     */
    public CertificateType getCertificateType() {
        return certificateType;
    }

    /**
     * The type of certificate
     *
     * @param certificateType The type
     * @since 1.0.0
     */
    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    /**
     * The visibility and position of the signature image rectangle
     *
     * @return The rectangle properties
     * @since 1.0.0
     */
    public SignRectangle getSignRectangle() {
        return signRectangle;
    }

    /**
     * The visibility and position of the signature image rectangle
     *
     * @param signRectangle The rectangle properties
     * @since 1.0.0
     */
    public void setSignRectangle(SignRectangle signRectangle) {
        this.signRectangle = signRectangle;
    }

    /**
     * Define if the signature is visible or not
     *
     * @return bool
     * @since 1.0.0
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Define if the signature is visible or not
     *
     * @param visible bool
     * @since 1.0.0
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * The legend 'Location' to be write in the signature
     *
     * @return The Location
     * @since 1.0.0
     */
    public String getLocation() {
        return location;
    }

    /**
     * The legend 'Location' to be write in the signature
     *
     * @param location The Location
     * @since 1.0.0
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * The legend 'Reason' to be write in the signature
     *
     * @return The Reason
     * @since 1.0.0
     */
    public String getReason() {
        return reason;
    }

    /**
     * The legend 'Reason' to be write in the signature
     *
     * @param reason Reason
     * @since 1.0.0
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Set a contact
     *
     * @return The contact
     * @since 1.0.0
     */
    public String getContact() {
        return contact;
    }

    /**
     * Get the contact
     *
     * @param contact The contact
     * @since 1.0.0
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sign sign = (Sign) o;
        return visible == sign.visible
                && Objects.equals(keyStorePassword, sign.keyStorePassword)
                && Objects.equals(certificateName, sign.certificateName)
                && Objects.equals(certificatePassword, sign.certificatePassword)
                && level == sign.level
                && certificateType == sign.certificateType
                && Objects.equals(signRectangle, sign.signRectangle)
                && Objects.equals(location, sign.location)
                && Objects.equals(reason, sign.reason)
                && Objects.equals(contact, sign.contact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyStorePassword, certificateName, certificatePassword, level, certificateType, signRectangle, visible, location, reason);
    }

    @Override
    public String toString() {
        return "Sign{" +
                "keyStorePassword='*****'" +
                ", certificateName='" + certificateName + '\'' +
                ", certificatePassword='****'" +
                ", level=" + level +
                ", certificateType=" + certificateType +
                ", signRectangle=" + signRectangle +
                ", visible=" + visible +
                ", location='" + location + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
