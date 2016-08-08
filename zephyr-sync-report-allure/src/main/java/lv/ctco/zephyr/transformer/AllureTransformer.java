package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class AllureTransformer implements ReportTransformer {

    public String getType() {
        return "allure";
    }

    public List<TestCase> transformToTestCases(String reportPath) {
        return transform(readAllureReport(reportPath));
    }

    List<TestSuiteResult> readAllureReport(String path) {
        File file = new File(path);
        try {
            if (file.isDirectory()) {
                return AllureFileUtils.unmarshalSuites(file);
            } else {
                return singletonList(AllureFileUtils.unmarshal(file));
            }
        } catch (IOException e) {
            throw new ZephyrSyncException("Cannot read Allure report", e);
        }
    }

    List<TestCase> transform(List<TestSuiteResult> result) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        for (TestSuiteResult currentTestSuiteResult : result) {
            for (TestCaseResult currentTestCaseResult : currentTestSuiteResult.getTestCases()) {
                TestCase currentTestCase = new TestCase();
                currentTestCase.setName(currentTestCaseResult.getName());
                currentTestCase.setUniqueId(generateUniqueId(currentTestCaseResult));
                currentTestCase.setStoryKeys(getStoryKeys(currentTestCaseResult));
                currentTestCase.setKey(getJiraKey(currentTestCaseResult));
                currentTestCase.setStatus(getStatus(currentTestCaseResult));
                currentTestCase.setSeverity(getSeverity(currentTestCaseResult));
                currentTestCase.setSteps(addTestSteps(currentTestCaseResult.getSteps(), 1));
                testCases.add(currentTestCase);
            }
        }
        return testCases;
    }

    private String generateUniqueId(TestCaseResult testCaseResult) {
        return testCaseResult.getName();
    }

    private TestStatus getStatus(TestCaseResult testCaseResult) {
        switch (testCaseResult.getStatus()) {
            case FAILED:
                return TestStatus.FAILED;
            case BROKEN:
                return TestStatus.FAILED;
            case PASSED:
                return TestStatus.PASSED;
            default:
                return TestStatus.NOT_EXECUTED;
        }
    }

    private TestLevel getSeverity(TestCaseResult currentTestCaseResult) {
        String severity = "";

        for (Label currentLabel : currentTestCaseResult.getLabels()) {
            if (currentLabel.getName().equalsIgnoreCase("severity") && !currentLabel.getValue().isEmpty()) {
                severity = currentLabel.getValue();
            }
        }
        if (!(severity.isEmpty())) {
            switch (SeverityLevel.fromValue(severity)) {
                case TRIVIAL:
                    return TestLevel.TRIVIAL;
                case MINOR:
                    return TestLevel.MINOR;
                case CRITICAL:
                    return TestLevel.CRITICAL;
                case BLOCKER:
                    return TestLevel.BLOCKER;
                default:
                    return TestLevel.MAJOR;
            }

        }
        return null;
    }

    private List<String> getStoryKeys(TestCaseResult currentTestCaseResult) {
        List<String> storyKeys = new ArrayList<String>();

        for (Label currentLabel : currentTestCaseResult.getLabels()) {
            if (currentLabel.getName().equalsIgnoreCase("story") && !currentLabel.getValue().isEmpty()) {
                storyKeys.add(currentLabel.getValue());
            }
        }
        return storyKeys.size() != 0 ? storyKeys : null;
    }

    private String getJiraKey(TestCaseResult currentTestCaseResult) {
        for (Label currentLabel : currentTestCaseResult.getLabels()) {
            if (currentLabel.getName().equalsIgnoreCase("testid") && !currentLabel.getValue().isEmpty()) {
                return currentLabel.getValue().toUpperCase();
            }
        }
        return null;
    }

    private List<TestStep> addTestSteps(List<Step> steps, int level) {
        List<TestStep> result = new ArrayList<TestStep>(steps.size());
        for (Step step : steps) {
            TestStep testStep = new TestStep();
            testStep.setDescription(step.getName());
            testStep.setSteps(addTestSteps(step.getSteps(), level + 1));
            result.add(testStep);
        }
        return result;
    }
}