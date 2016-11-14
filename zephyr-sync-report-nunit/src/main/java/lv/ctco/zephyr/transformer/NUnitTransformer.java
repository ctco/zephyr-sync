package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.testresult.cucumber.NUnitResultTestRun;
import lv.ctco.zephyr.beans.testresult.cucumber.NUnitTestCase;
import lv.ctco.zephyr.beans.testresult.cucumber.NUnitTestSuite;
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

    List<NUnitTestSuite> testSuites = new ArrayList<>();

    public List<TestCase> transformToTestCases(String reportPath) {
        try {
            return transform(readNunitReport(reportPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    NUnitResultTestRun readNunitReport(String path) throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(NUnitResultTestRun.class);

            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(path));
            xsr = xif.createFilteredReader(xsr, reader -> {
                if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
                    return reader.getText().trim().length() > 0;
                }
                return true;
            });
            return (NUnitResultTestRun) jaxbContext.createUnmarshaller().unmarshal(xsr);
        } catch (JAXBException e) {
            throw new ZephyrSyncException("Cannot process NUnit report", e);
        }
    }

    List<TestCase> transform(NUnitResultTestRun resultTestSuite) {
        // NUnit places project name to the first test suite
        List<NUnitTestSuite> nUnitTestSuites = resultTestSuite.getTestSuite().get(0).flattenTestSuite();
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