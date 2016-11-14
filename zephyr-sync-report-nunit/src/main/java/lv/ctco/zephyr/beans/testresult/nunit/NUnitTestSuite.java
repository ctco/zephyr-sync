package lv.ctco.zephyr.beans.testresult.nunit;

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
    private List<NUnitTestSuite> testSuite;

    @XmlElement(name = "test-case")
    private List<NUnitTestCase> testcase;

    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "type")
    private String type;

    public List<NUnitTestSuite> getTestSuite() {
        if (testSuite == null) {
            testSuite = new ArrayList<>();
        }
        return this.testSuite;
    }

    public List<NUnitTestCase> getTestCases() {
        if (testcase == null) {
            testcase = new ArrayList<>();
        }
        return this.testcase;
    }

    public List<NUnitTestSuite> flattenTestSuite() {
        List<NUnitTestSuite> result = new ArrayList<>();
        result.add(this);
        for (NUnitTestSuite nUnitTestSuite : getTestSuite()) {
            result.addAll(nUnitTestSuite.flattenTestSuite());
        }

        return result;
    }
}
