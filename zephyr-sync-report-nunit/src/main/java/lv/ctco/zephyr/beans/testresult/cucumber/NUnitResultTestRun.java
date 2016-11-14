package lv.ctco.zephyr.beans.testresult.cucumber;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "test-run", propOrder = {
        "testSuites"
})
@XmlRootElement(name = "test-run")
public class NUnitResultTestRun {

    @XmlElement(name = "test-suite")
    List<NUnitTestSuite> testSuites;

    public List<NUnitTestSuite> getTestSuite() {
        if (testSuites == null) {
            testSuites = new ArrayList<>();
        }
        return this.testSuites;
    }
}
