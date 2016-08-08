package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResult;
import lv.ctco.zephyr.beans.testresult.junit.JUnitResultTestSuite;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.Utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JUnitTransformer implements ReportTransformer {

    public String getType() {
        return "junit";
    }

    public List<TestCase> transformToTestCases(String reportPath) {
        return transform(readJUnitReport(reportPath));
    }

    JUnitResultTestSuite readJUnitReport(String path)  {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JUnitResultTestSuite.class);
            return (JUnitResultTestSuite) jaxbContext.createUnmarshaller().unmarshal(new File(path));
        } catch (JAXBException e) {
            throw new ZephyrSyncException("Cannot process JUnit report", e);
        }
    }

    List<TestCase> transform(JUnitResultTestSuite resultTestSuite) {
        if (resultTestSuite.getTestcase() == null) {
            return new ArrayList<TestCase>();
        }

        List<TestCase> result = new ArrayList<TestCase>();
        for (JUnitResult testCase : resultTestSuite.getTestcase()) {
            TestCase test = new TestCase();
            test.setName(testCase.getName());
            test.setUniqueId(generateUniqueId(testCase));
            test.setStatus(testCase.getError() != null || testCase.getFailure() != null ? TestStatus.FAILED : TestStatus.PASSED);
            result.add(test);
        }
        return result;
    }

    String generateUniqueId(JUnitResult testCase) {
        return String.join("-", Utils.normalizeKey(testCase.getClassname()), Utils.normalizeKey(testCase.getName()));
    }
}