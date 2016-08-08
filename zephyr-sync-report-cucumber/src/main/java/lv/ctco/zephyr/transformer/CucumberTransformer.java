package lv.ctco.zephyr.transformer;


import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.beans.testresult.cucumber.Feature;
import lv.ctco.zephyr.beans.testresult.cucumber.Scenario;
import lv.ctco.zephyr.beans.testresult.cucumber.Step;
import lv.ctco.zephyr.beans.testresult.cucumber.Tag;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.ObjectTransformer;
import lv.ctco.zephyr.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CucumberTransformer implements ReportTransformer {

    public String getType() {
        return "cucumber";
    }

    public List<TestCase> transformToTestCases(String reportPath) {
        return transform(readCucumberReport(reportPath));
    }

    String readCucumberReport(String path) {
        try {
            return Utils.readInputStream(new FileInputStream(new File(path)));
        } catch (IOException e) {
            throw new ZephyrSyncException("Cannot read cucumber report", e);
        }
    }

    List<TestCase> transform(String response) {
        List<Feature> features = ObjectTransformer.deserializeList(response, Feature.class);
        List<TestCase> testCases = new ArrayList<TestCase>();

        for (Feature feature : features) {
            Scenario[] scenarios = feature.getScenarios();
            if (scenarios == null) {
                continue;
            }

            for (Scenario scenario : scenarios) {
                TestCase test = new TestCase();
                List<String> jiraKeys = resolveJiraKeys(scenario, "@TestCaseId=");
                if (jiraKeys != null && jiraKeys.size() == 1) {
                    test.setKey(jiraKeys.get(0).toUpperCase());
                }
                test.setName(scenario.getName() == null ? feature.getName() : scenario.getName());
                test.setUniqueId(generateUniqueId(feature, scenario));
                test.setStoryKeys(resolveJiraKeys(scenario, "@Stories="));
                test.setStatus(resolveStatus(scenario));
                test.setSteps(resolveTestSteps(scenario));
                testCases.add(test);
            }
        }
        return testCases;
    }

    public String generateUniqueId(Feature feature, Scenario scenario) {
        String id = scenario.getId();
        if (id == null) {
            id = feature.getId();
        }
        String[] tokens = id.replace(";", "-").replaceAll("[^a-zA-Z0-9\\-]", "").split("-");
        StringBuilder sb = new StringBuilder();
        for (String token : tokens) {
            if (token.length() > 0) {
                sb.append(token.charAt(0));
            }
        }
        return sb.toString().toUpperCase();
    }

    private TestStatus resolveStatus(Scenario scenario) {
        if (scenario.getSteps() == null) {
            return TestStatus.PASSED;
        }
        for (Step step : scenario.getSteps()) {
            if (step.getResult() != null && !"passed".equals(step.getResult().getStatus())) {
                return TestStatus.FAILED;
            }
        }
        return TestStatus.PASSED;
    }

    private List<String> resolveJiraKeys(Scenario scenario, String tagPrefix) {
        Tag[] tags = scenario.getTags();
        if (tags == null || tags.length == 0) return null;

        List<String> result = new ArrayList<String>();
        for (Tag tag : tags) {
            String tagName = tag.getName();

            if (tagName.toLowerCase().startsWith(tagPrefix.toLowerCase())) {
                String[] keys = tagName.substring(tagPrefix.length()).trim().split(",");
                Collections.addAll(result, keys);
            }
        }
        return result.size() != 0 ? result : null;
    }

    private List<TestStep> resolveTestSteps(Scenario scenario) {
        List<TestStep> result = new ArrayList<TestStep>();
        if (scenario.getSteps() != null) {
            for (Step step : scenario.getSteps()) {
                result.add(new TestStep(step.getKeyword() + step.getName()));
            }
        }
        return result;
    }
}