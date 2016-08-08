package lv.ctco.zephyr.beans.testresult.junit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "error",
        "failure"
})
public class JUnitResult {

    protected Error error;
    protected Failure failure;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String name;
    @XmlAttribute(name = "classname", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String classname;
    @XmlAttribute(name = "time", required = true)
    protected BigDecimal time;

    public Error getError() {
        return error;
    }

    public void setError(Error value) {
        this.error = value;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure value) {
        this.failure = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String value) {
        this.classname = value;
    }

    public BigDecimal getTime() {
        return time;
    }

    public void setTime(BigDecimal value) {
        this.time = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class Error {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "message")
        protected String message;
        @XmlAttribute(name = "type", required = true)
        protected String type;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String value) {
            this.message = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String value) {
            this.type = value;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "value"
    })
    public static class Failure {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "message")
        protected String message;
        @XmlAttribute(name = "type", required = true)
        protected String type;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String value) {
            this.message = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String value) {
            this.type = value;
        }
    }
}