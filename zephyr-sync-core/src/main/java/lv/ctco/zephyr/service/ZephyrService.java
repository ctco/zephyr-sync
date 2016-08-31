package lv.ctco.zephyr.service;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.zapi.Execution;
import lv.ctco.zephyr.beans.zapi.ExecutionRequest;
import lv.ctco.zephyr.beans.zapi.ExecutionResponse;
import lv.ctco.zephyr.beans.zapi.ZapiTestStep;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.enums.TestStatus;
import lv.ctco.zephyr.util.HttpUtils;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lv.ctco.zephyr.enums.ConfigProperty.*;
import static lv.ctco.zephyr.util.HttpUtils.*;
import static lv.ctco.zephyr.util.Utils.log;
import static java.lang.String.format;

public class ZephyrService {

    private static final int TOP = 20;

    private Config config;

    public ZephyrService(Config config) {
        this.config = config;
    }

    public Map<String, Execution> getAllExecutions(Config config) throws IOException {
        log("Fetching JIRA Test Executions for the project");
        int skip = 0;
        String search = "project='" + config.getValue(PROJECT_KEY) + "'%20and%20fixVersion='"
                + URLEncoder.encode(config.getValue(RELEASE_VERSION), "UTF-8") + "'%20and%20cycleName='" + config.getValue(TEST_CYCLE) + "'";

        ExecutionResponse executionResponse = searchInZQL(search, skip);
        if (executionResponse == null || executionResponse.getExecutions().isEmpty()) {
            return new HashMap<String, Execution>();
        }

        List<Execution> executions = executionResponse.getExecutions();

        int totalCount = executionResponse.getTotalCount();
        if (totalCount > TOP) {
            while (executions.size() != totalCount) {
                skip += TOP;
                List<Execution> nextPageExecutions = searchInZQL(search, skip).getExecutions();
                if (nextPageExecutions.isEmpty()) {
                    break;
                }
                executions.addAll(nextPageExecutions);
            }
        }
        Map<String, Execution> result = new HashMap<String, Execution>(executions.size());
        for (Execution execution : executions) {
            result.put(execution.getIssueKey(), execution);
        }
        log(format("Retrieved %s Test executions\n", executions.size()));
        return result;
    }

    ExecutionResponse searchInZQL(String search, int skip) throws IOException {
        String response = getAndReturnBody(config, "zapi/latest/zql/executeSearch?zqlQuery=" + search + "&offset=" + skip);
        return ObjectTransformer.deserialize(response, ExecutionResponse.class);
    }

    public void linkExecutionsToTestCycle(MetaInfo metaInfo, List<TestCase> testCases) throws IOException {
        Map<String, Execution> executions = getAllExecutions(config);

        List<String> keys = new ArrayList<String>();

        for (TestCase testCase : testCases) {
            if (!executions.containsKey(testCase.getKey())) {
                keys.add(testCase.getKey());
            }
        }
        if (keys.size() > 0) {
            linkTestToCycle(metaInfo, keys);
        } else {
            log("All Test cases are already linked to the Test cycle.\n");
        }

    }

    void linkTestToCycle(MetaInfo metaInfo, List<String> keys) throws IOException {
        log("Linking Test cases " + keys.toString() + " to Test Cycle");

        Execution execution = new Execution();
        execution.setProjectId(metaInfo.getProjectId());
        execution.setVersionId(metaInfo.getVersionId());
        execution.setCycleId(metaInfo.getCycleId());
        execution.setMethod(1);
        execution.setIssues(keys);

        HttpResponse response = post(config, "zapi/latest/execution/addTestsToCycle", execution);
        ensureResponse(response, 200, "Could not link Test cases");
    }

    public void updateExecutionStatuses(List<TestCase> resultTestCases) throws IOException {
        Map<String, Execution> executions = getAllExecutions(config);

        Map<TestStatus, List<String>> statusMap = new HashMap<TestStatus, List<String>>();

        for (TestCase testCase : resultTestCases) {
            TestStatus status = testCase.getStatus();
            List<String> ids = statusMap.get(status);
            if (ids == null) {
                statusMap.put(status, new ArrayList<String>());
            }
            Execution execution = executions.get(testCase.getKey());
            if (execution != null) {
                statusMap.get(status).add(execution.getId().toString());
            }
        }

        for (Map.Entry<TestStatus, List<String>> entry : statusMap.entrySet()) {
            for (String id : entry.getValue()) {
                updateExecutionStatus(entry.getKey(), id);
            }
        }
    }

    void updateExecutionStatus(TestStatus status, String id) throws IOException {
        log("Setting status " + status.name() + " to " + id + " test case");

        ExecutionRequest request = new ExecutionRequest();
        request.setStatus(status.getId());

        HttpResponse response = put(config, "zapi/latest/execution/" + id + "/execute", request);
        ensureResponse(response, 200, "Could not successfully update execution status");
    }

    public void addStepsToTestIssue(TestCase testCase) throws IOException {
        log("Adding Test steps to Test issue: " + testCase.getKey());
        List<TestStep> testSteps = testCase.getSteps();

        Map<Integer, TestStep> map = new HashMap<Integer, TestStep>();
        prepareTestSteps(map, testSteps, 0, "", Boolean.valueOf(config.getValue(ORDERED_STEPS)));

        for (TestStep step : map.values()) {
            HttpResponse response = post(config, "zapi/latest/teststep/" + testCase.getId(), new ZapiTestStep(step.getDescription()));
            ensureResponse(response, 200, "Could not add Test Steps for Test Case: " + testCase.getId());
        }
    }

    void prepareTestSteps(Map<Integer, TestStep> map, List<TestStep> testSteps, int level, String prefix, Boolean isOrdered) {
        for (int i = 1; i <= testSteps.size(); i++) {
            TestStep testStep = testSteps.get(i - 1);
            String description = testStep.getDescription();
            testStep.setDescription(isOrdered ? format("%s %s", prefix + i + ".", description) : description);
            map.put(map.size() + 1, testStep);

            if (testStep.getSteps() != null && testStep.getSteps().size() > 0) {
                prepareTestSteps(map, testStep.getSteps(), level + 1, prefix + i + ".", isOrdered);
            }
        }
    }

    public void mapTestCasesToIssues(List<TestCase> resultTestCases, List<Issue> issues) {
        Map<String, Issue> uniqueKeyMap = new HashMap<String, Issue>(issues.size());
        for (Issue issue : issues) {
            uniqueKeyMap.put(issue.getKey(), issue);
            String testCaseUniqueId = issue.getFields().getTestCaseUniqueId();
            if (testCaseUniqueId != null) {
                uniqueKeyMap.put(testCaseUniqueId, issue);
            }
        }

        for (TestCase testCase : resultTestCases) {
            if (testCase.getKey() != null) {
                if (uniqueKeyMap.containsKey(testCase.getKey())) {
                    testCase.setId(uniqueKeyMap.get(testCase.getKey()).getId());
                } else {
                    log(format("Key: %s, is not found, new Test Case should be created", testCase.getKey()));
                    testCase.setId(null);
                    testCase.setKey(null);
                }
            } else {
                Issue issue = uniqueKeyMap.get(testCase.getUniqueId());
                if (issue == null) {
                    continue;
                }

                testCase.setId(issue.getId());
                testCase.setKey(issue.getKey());
            }
        }
    }

}
