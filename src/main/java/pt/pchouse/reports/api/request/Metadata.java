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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Metadata {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The document title
     * @since 1.0.0
     */
    private String title;

    /**
     * The document author
     * @since 1.0.0
     */
    private String author;

    /**
     * The document subject
     * @since 1.0.0
     */
    private String subject;

    /**
     * Document keywords
     * @since 1.0.0
     */
    private String keywords;

    /**
     * Application
     * @since 1.0.0
     */
    private String application;

    /**
     * The document creator
     * @since 1.0.0
     */
    private String creator;

    /**
     * Display meta title
     * @since 1.0.0
     */
    private boolean displayMetadataTitle = false;

    /**
     * Document metadata
     * @since 1.0.0
     */
    public Metadata() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the document title
     * @return Title
     * @since 1.0.0
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set document title
     * @param title Title
     * @since 1.0.0
     */
    public void setTitle(String title) {
        this.title = title;
        logger.debug("Title set to {}", this.title);
    }

    /**
     * Get document author
     * @return Author
     * @since 1.0.0
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the document author
     * @param author Author
     * @since 1.0.0
     */
    public void setAuthor(String author) {
        this.author = author;
        logger.debug("Author set to {}", this.author);
    }

    /**
     * Get the document subject
     * @return Subject
     * @since 1.0.0
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the document subject
     * @param subject Subject
     * @since 1.0.0
     */
    public void setSubject(String subject) {
        this.subject = subject;
        logger.debug("Subject set to {}", this.subject);
    }

    /**
     * Get the document keywords
     * @return Keywords
     * @since 1.0.0
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Set the document keywords
     * @param keywords Keywords
     * @since 1.0.0
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
        logger.debug("Keywords set to {}", this.keywords);
    }

    /**
     * Get application name
     * @return Name
     * @since 1.0.0
     */
    public String getApplication() {
        return application;
    }

    /**
     * Set application name
     * @param application Name
     * @since 1.0.0
     */
    public void setApplication(String application) {
        this.application = application;
        logger.debug("Application set to {}", this.application);
    }

    /**
     * Get document creator
     * @return Creator
     * @since 1.0.0
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Set document creator
     * @param creator Creator
     * @since 1.0.0
     */
    public void setCreator(String creator) {
        this.creator = creator;
        logger.debug("Creator set to {}", this.creator);
    }

    /**
     * Get if the document title is to be displayed
     * @return True if is to be displayed
     * @since 1.0.0
     */
    public boolean isDisplayMetadataTitle() {
        return displayMetadataTitle;
    }

    /**
     * Set if the document is to be displayed
     * @param displayMetadataTitle True if is to be displayed
     * @since 1.0.0
     */
    public void setDisplayMetadataTitle(boolean displayMetadataTitle) {
        this.displayMetadataTitle = displayMetadataTitle;
        logger.debug("Display metadata title set to {}", this.isDisplayMetadataTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return displayMetadataTitle == metadata.displayMetadataTitle
                && Objects.equals(title, metadata.title)
                && Objects.equals(author, metadata.author)
                && Objects.equals(subject, metadata.subject)
                && Objects.equals(keywords, metadata.keywords)
                && Objects.equals(application, metadata.application)
                && Objects.equals(creator, metadata.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                title, author, subject, keywords, application, creator, displayMetadataTitle
        );
    }
}
