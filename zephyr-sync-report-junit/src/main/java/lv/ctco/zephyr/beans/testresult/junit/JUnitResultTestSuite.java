
package lv.ctco.zephyr.beans.testresult.junit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testsuite", propOrder = {
        "properties",
        "testcase",
        "systemOut",
        "systemErr"
})
@XmlRootElement(name = "testsuite")
public class JUnitResultTestSuite {

    @XmlElement(required = true)
    protected JUnitTCProperties properties;
    protected List<JUnitResult> testcase;
    @XmlElement(name = "system-out", required = true)
    protected String systemOut;
    @XmlElement(name = "system-err", required = true)
    protected String systemErr;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "timestamp", required = true)
    protected XMLGregorianCalendar timestamp;
    @XmlAttribute(name = "hostname", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String hostname;
    @XmlAttribute(name = "tests", required = true)
    protected int tests;
    @XmlAttribute(name = "failures", required = true)
    protected int failures;
    @XmlAttribute(name = "errors", required = true)
    protected int errors;
    @XmlAttribute(name = "time", required = true)
    protected BigDecimal time;

    public JUnitTCProperties getProperties() {
        return properties;
    }

    public void setProperties(JUnitTCProperties value) {
        this.properties = value;
    }

    public List<JUnitResult> getTestcase() {
        if (testcase == null) {
            testcase = new ArrayList<JUnitResult>();
        }
        return this.testcase;
    }

    public String getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(String value) {
        this.systemOut = value;
    }

    public String getSystemErr() {
        return systemErr;
    }

    public void setSystemErr(String value) {
        this.systemErr = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String value) {
        this.hostname = value;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int value) {
        this.tests = value;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int value) {
        this.failures = value;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int value) {
        this.errors = value;
    }

    public BigDecimal getTime() {
        return time;
    }

    public void setTime(BigDecimal value) {
        this.time = value;
    }
}