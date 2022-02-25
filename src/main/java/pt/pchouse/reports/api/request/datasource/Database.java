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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * The database datasource definitions
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Database {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The database jdbc connection string
     * Ex: <br>
     * <code>jdbc:oracle:thin:@//localhost:1521/XE</code>
     * @since 1.0.0
     */
    private String connectionString;

    /**
     * The driver class name to be used to connect to the database
     * Ex:<br>
     * <code>oracle.jdbc.OracleDriver</code><br>
     * <code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code>
     * @since 1.0.0
     */
    private String driver;

    /**
     * The username to connect to the database
     * @since 1.0.0
     */
    private String user;

    /**
     * The password to connect to the database
     * @since 1.0.0
     */
    private String password;

    /**
     * The database datasource
     * @since 1.0.0
     */
    public Database() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * The database datasource
     *
     * @param connectionString The database connection string
     * @param driver           The driver class name
     * @param user             The database login user
     * @param password         The database login password
     * @since 1.0.0
     */
    public Database(String connectionString, String driver, String user, String password) {
        logger.debug("New instance of {}", this.getClass().getName());
        setConnectionString(connectionString);
        setDriver(driver);
        setUser(user);
        setPassword(password);
    }

    /**
     * The database jdbc connection string
     * Ex: <br>
     * <code>jdbc:oracle:thin:@//localhost:1521/XE</code>
     *
     * @return The connection string to be used by the jdbc driver to connect to database
     * @since 1.0.0
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * The database jdbc connection string
     * Ex: <br>
     * <code>jdbc:oracle:thin:@//localhost:1521/XE</code>
     *
     * @param connectionString The connection string to be used by the jdbc driver to connect to database
     * @since 1.0.0
     */
    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
        logger.debug(
                "Connection string set to {}",
                this.connectionString == null ? "null" : this.connectionString
        );
    }

    /**
     * The driver class name to be used to connect to the database
     * Ex:<br>
     * <code>oracle.jdbc.OracleDriver</code><br>
     * <code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code>
     *
     * @return The jdbc driver class name to be used in the datasource
     * @since 1.0.0
     */
    public String getDriver() {
        return driver;
    }

    /**
     * The driver class name to be used to connect to the database
     * Ex:<br>
     * <code>oracle.jdbc.OracleDriver</code><br>
     * <code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code>
     *
     * @param driver The jdbc driver class name to be used in the datasource
     * @since 1.0.0
     */
    public void setDriver(String driver) {
        this.driver = driver;
        logger.debug(
                "Driver set to {}",
                this.driver == null ? "null" : this.driver
        );
    }

    /**
     * The username to connect to the database
     *
     * @return The username to be used to log in database
     * @since 1.0.0
     */
    public String getUser() {
        return user;
    }

    /**
     * The username to connect to the database
     *
     * @param user The username to be used to log in database
     * @since 1.0.0
     */
    public void setUser(String user) {
        this.user = user;
        logger.debug(
                "User set to {}",
                this.user == null ? "null" : this.user
        );
    }

    /**
     * The password to connect to the database
     *
     * @return The password to be used to log in database
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * The password to connect to the database
     *
     * @param password The password to be used to log in database
     * @since 1.0.0
     */
    public void setPassword(String password) {
        this.password = password;
        logger.debug(
                "Password set to {}",
                this.password == null ? "null" : StringUtils.leftPad("", this.password.length(), '*')
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return connectionString.equals(database.connectionString)
                && driver.equals(database.driver)
                && user.equals(database.user)
                && password.equals(database.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionString, driver, user, password);
    }

    @Override
    public String toString() {
        return "Database{" +
                "connectionString='" + connectionString + '\'' +
                ", driver='" + driver + '\'' +
                ", user='" + user + '\'' +
                ", password='" + (password == null ? "null" : "****") + '\'' +
                '}';
    }
}
