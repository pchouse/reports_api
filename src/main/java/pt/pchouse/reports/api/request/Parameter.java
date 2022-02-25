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
public class Parameter {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The parameter type value
     * @since 1.0.0
     */
    public enum Types {
        P_STRING,
        P_BOOLEAN,
        P_BOOL,
        P_DOUBLE,
        P_FLOAT,
        P_INTEGER,
        P_LONG,
        P_SHORT,
        P_BIGDECIMAL,
        P_DATE,
        P_TIME,
        P_SQL_TIME,
        P_SQL_DATE,
        P_TIMESTAMP,
    }

    /**
     * The report parameter data type
     * @since 1.0.0
     */
    private Types type;

    /**
     * The report parameter name
     * @since 1.0.0
     */
    private String name;

    /**
     * The parameter value
     * @since 1.0.0
     */
    private String value;

    /**
     * The date format for SimpleDateFormat, and only where the parameter type
     * is "date", for other types must be empty or null
     * @since 1.0.0
     */
    private String format;

    /**
     * Report parameters
     * @since 1.0.0
     */
    public Parameter() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Report parameters
     *
     * @param types Type of parameter
     * @param name  The parameter's name
     * @param value The parameter's value
     * @since 1.0.0
     */
    public Parameter(Types types, String name, String value) {
        logger.debug("New instance of {}", this.getClass().getName());
        setType(types);
        setName(name);
        setValue(value);
        setFormat(null);
    }

    /**
     *
     * @param types The parameter type
     * @param name The parameter name
     * @param value The parameter value
     * @param format The parameter format
     * @since 1.0.0
     */
    public Parameter(Types types, String name, String value, String format) {
        logger.debug("New instance of {}", this.getClass().getName());
        setType(types);
        setName(name);
        setValue(value);
        setFormat(format);
    }

    /**
     * The report parameter data type
     *
     * @return The type
     * @since 1.0.0
     */
    public Types getType() {
        return type;
    }

    /**
     * The report parameter data type
     *
     * @param type The type
     * @since 1.0.0
     */
    public void setType(Types type) {
        this.type = type;
        logger.debug(
                "Type set to {}",
                this.type == null ? "null" : this.type.toString()
        );
    }

    /**
     * The report parameter name
     *
     * @return The name
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * The report parameter name
     *
     * @param name The name
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
        logger.debug(
                "Name set to {}",
                this.name == null ? "null" : this.name
        );
    }

    /**
     * The parameter value
     *
     * @return The value
     * @since 1.0.0
     */
    public String getValue() {
        if (type != null && (type.equals(Types.P_BOOL) || type.equals(Types.P_BOOLEAN))) {
            if (value.equals("1") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on")) {
                return "true";
            }
        }
        return value;
    }

    /**
     * The parameter value
     *
     * @param value The value
     * @since 1.0.0
     */
    public void setValue(String value) {
        this.value = value;
        logger.debug(
                "Value set to {}",
                this.value == null ? "null" : this.value
        );
    }

    /**
     * The date format for SimpleDateFormat, and only where the parameter type
     * is "date", for other types must be empty or null
     *
     * @return The date format
     * @since 1.0.0
     */
    public String getFormat() {
        return format;
    }

    /**
     * The date format for SimpleDateFormat, and only where the parameter type
     * is "date", for other types must be empty or null
     *
     * @param format The date format
     * @since 1.0.0
     */
    public void setFormat(String format) {
        this.format = format;
        logger.debug(
                "Format set to {}",
                this.format == null ? "null" : this.format
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameter parameter = (Parameter) o;
        return type == parameter.type
                && Objects.equals(name, parameter.name)
                && Objects.equals(value, parameter.value)
                && Objects.equals(format, parameter.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, value, format);
    }
}
