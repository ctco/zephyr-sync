package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testresult.nunit.NUnitResultTestRun;
import lv.ctco.zephyr.beans.testresult.nunit.NUnitTestCase;
import lv.ctco.zephyr.beans.testresult.nunit.NUnitTestSuite;
import lv.ctco.zephyr.enums.TestStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.List;


public class NUnitTransformer implements ReportTransformer {

    public String getType() {
        return "nunit";
    }

    public List<TestCase> transformToTestCases(String reportPath) {
            return transform(readNunitReport(reportPath));
    }

    NUnitResultTestRun readNunitReport(String path) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(NUnitResultTestRun.class);

            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(path));
            xsr = xif.createFilteredReader(xsr, r -> r.getEventType() != XMLStreamReader.CHARACTERS || r.getText().trim().length() > 0);
            return (NUnitResultTestRun) jaxbContext.createUnmarshaller().unmarshal(xsr);
        } catch (Exception e) {
            throw new ZephyrSyncException("Cannot process NUnit report", e);
        }
    }

    List<TestCase> transform(NUnitResultTestRun resultTestSuite) {
        // NUnit places project name to the first test suite
        List<NUnitTestSuite> nUnitTestSuites = resultTestSuite.getTestSuite().stream().findFirst().get().flattenTestSuite();
        // NUnit places test cases in the last test suite
        NUnitTestSuite lastTestSuite = nUnitTestSuites.get(nUnitTestSuites.size() - 1);
        List<TestCase> result = new ArrayList<TestCase>();
        for (NUnitTestCase testCase : lastTestSuite.getTestCases()) {
            TestCase test = new TestCase();
            test.setName(testCase.getName());
            test.setUniqueId(testCase.getId());
            test.setStatus(testCase.getResult().equals("Passed") ? TestStatus.PASSED : TestStatus.FAILED);
            result.add(test);
        }
        return result;
    }

}