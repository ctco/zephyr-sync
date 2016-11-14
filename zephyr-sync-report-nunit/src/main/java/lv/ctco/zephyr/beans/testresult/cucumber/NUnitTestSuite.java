package lv.ctco.zephyr.beans.testresult.cucumber;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "test-suite", propOrder = {
        "testSuite",
        "testcase"
})
@XmlRootElement(name = "test-suite")
public class NUnitTestSuite {

    @XmlMixed
    @XmlElementRef(name = "test-suite", type = NUnitTestSuite.class, required = true)
    protected List<NUnitTestSuite> testSuite;

    @XmlElement(name = "test-case")
    protected List<NUnitTestCase> testcase;

    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "type")
    protected String type;

    public List<NUnitTestSuite> getTestSuite() {
        if (testSuite == null) {
            testSuite = new ArrayList<NUnitTestSuite>();
        }
        return this.testSuite;
    }

    public List<NUnitTestCase> getTestCases() {
        if (testcase == null) {
            testcase = new ArrayList<NUnitTestCase>();
        }
        return this.testcase;
    }

    public List<NUnitTestSuite> flattenTestSuite() {
        List<NUnitTestSuite> result = new ArrayList<>();
        if (!getTestSuite().isEmpty()) result.addAll(testSuite);
        for (NUnitTestSuite nUnitTestSuite : getTestSuite()) {
            result.addAll(nUnitTestSuite.flattenTestSuite());
        }

        return result;
    }
}
