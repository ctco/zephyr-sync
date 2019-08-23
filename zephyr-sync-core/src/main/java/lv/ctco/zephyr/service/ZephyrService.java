package lv.ctco.zephyr.service;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.TestStep;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.zapi.Execution;
import lv.ctco.zephyr.beans.zapi.ExecutionRequest;
import lv.ctco.zephyr.beans.zapi.ExecutionResponse;
import lv.ctco.zephyr.beans.zapi.ZapiTestStep;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static lv.ctco.zephyr.enums.ConfigProperty.*;
import static lv.ctco.zephyr.util.HttpUtils.*;
import static lv.ctco.zephyr.util.Utils.log;

public class ZephyrService {

    private static final int TOP = 20;

    private Config config;

    public ZephyrService(Config config) {
        this.config = config;
    }

    private Map<String, Execution> getAllExecutions(Config config) throws IOException {
        log("Fetching JIRA Test Executions for the project");
        int skip = 0;
        String search = "project='" + config.getValue(PROJECT_KEY) + "'%20and%20fixVersion='"
                + URLEncoder.encode(config.getValue(RELEASE_VERSION), "UTF-8") + "'%20and%20cycleName='" + config.getValue(TEST_CYCLE) + "'";

        ExecutionResponse executionResponse = searchInZQL(search, skip);
        if (executionResponse == null || executionResponse.getExecutions().isEmpty()) {
            return new HashMap<>();
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
        Map<String, Execution> result = new HashMap<>(executions.size());
        for (Execution execution : executions) {
            result.put(execution.getIssueKey(), execution);
        }
        log(format("Retrieved %s Test executions\n", executions.size()));
        return result;
    }

    private ExecutionResponse searchInZQL(String search, int skip) throws IOException {
        String response = getAndReturnBody(config, "zapi/latest/zql/executeSearch?zqlQuery=" + search + "&offset=" + skip);
        return ObjectTransformer.deserialize(response, ExecutionResponse.class);
    }

    public void linkExecutionsToTestCycle(MetaInfo metaInfo, List<TestCase> testCases) throws IOException, InterruptedException {
        Map<String, Execution> executions = getAllExecutions(config);

        List<String> keys = new ArrayList<>();

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

    private void linkTestToCycle(MetaInfo metaInfo, List<String> keys) throws IOException, InterruptedException {
        log("INFO: Linking Test Cases to Test Cycle:" + keys.toString() + "");

        Execution execution = new Execution();
        execution.setCycleId(metaInfo.getCycleId());
        execution.setIssues(keys);
        execution.setMethod("1");
        execution.setProjectId(metaInfo.getProjectId());
        execution.setVersionId(metaInfo.getVersionId());

        /*
         On first execution adding items to test cycle may take long time,
         since that job is ran async, it may cause the situation when limited amount of tests will be returned by getAllExecutions()
         in ZAPI version 2.3.0 Atlassian made addTestsToCycle to return jobProgressToken to check job status
         prooflink1: https://marketplace.atlassian.com/plugins/com.thed.zephyr.zapi/versions
         prooflink2: http://docs.getzephyr.apiary.io/#reference/executionresource/add-tests-to-cycle/add-test%27s-to-cycle
        */

        // TODO: for ZAPI version 2.3.0 or higher implement handling of jobProgressToken
        HttpResponse response = post(config, "zapi/latest/execution/addTestsToCycle/", execution);
        ensureResponse(response, 200, "Could not link Test cases");

        // waiting for addTestsToCycle() to finish it's job, workaround/hack/waiter for ZAPI version lover than 2.3.0
        int iteration = 0;
        while (!checkTestCycleIsInSync(keys) && iteration < 5) {
            log("INFO: Test Cycle is not in sync. Giving Zephyr another chance.");
            Thread.sleep(5000);
            iteration++;
        }
    }

    private boolean checkTestCycleIsInSync(List<String> keys) throws IOException {
        Map<String, Execution> executions = getAllExecutions(config);
        for (String testId : keys) {
            if (!executions.containsKey(testId)) {
                log("INFO: Test Case " + testId + " was not found in Test Cycle");
                return false;
            }
        }
        return true;
    }

    public void updateExecutionStatuses(List<TestCase> testCases) throws IOException {
        Map<String, Execution> executions = getAllExecutions(config);

        for (TestCase testCase : testCases) {
            log("INFO: Setting status " + testCase.getStatus() + " for Test: " + testCase.getKey() + "");

            Execution execution = executions.get(testCase.getKey());
            if (execution == null) {
                log("WARN: Test " + testCase.getKey() + " not found in Test Cycle " + config.getValue(TEST_CYCLE) + "");
                continue;
            }

            ExecutionRequest request = new ExecutionRequest();
            request.setStatus(testCase.getStatus().getId());
            HttpResponse response = put(config, "zapi/latest/execution/" + execution.getId() + "/execute", request);
            ensureResponse(response, 200, "Could not successfully update execution status");
        }
    }

    public void addStepsToTestIssue(TestCase testCase) throws IOException {
        log("INFO: Getting Test Steps for Test: " + testCase.getKey());
        List<TestStep> testSteps = testCase.getSteps();
        if (testSteps == null) {
            log("INFO: No Test Steps found for Test: " + testCase.getKey());
            return;
        }

        Map<Integer, TestStep> map = prepareTestSteps(testSteps, 0, "", Boolean.valueOf(config.getValue(ORDERED_STEPS)));
        log("INFO: Setting Test Steps for Test: " + testCase.getKey());
        for (TestStep step : map.values()) {
            HttpResponse response = post(config, "zapi/latest/teststep/" + testCase.getId(), new ZapiTestStep(step.getDescription()));
            ensureResponse(response, 200, "Could not add Test Steps for Test Case: " + testCase.getId());
        }
    }

    private Map<Integer, TestStep> prepareTestSteps(List<TestStep> testSteps, int level, String prefix, Boolean isOrdered) {
        Map<Integer, TestStep> map = new HashMap<>();
        for (int i = 1; i <= testSteps.size(); i++) {
            TestStep testStep = testSteps.get(i - 1);
            String description = testStep.getDescription();
            testStep.setDescription(isOrdered ? format("%s %s", prefix + i + ".", description) : description);
            map.put(map.size() + 1, testStep);

            if (testStep.getSteps() != null && testStep.getSteps().size() > 0) {
                map.putAll(prepareTestSteps(testStep.getSteps(), level + 1, prefix + i + ".", isOrdered));

            }
        }

        return map;
    }

    public void mapTestCasesToIssues(List<TestCase> resultTestCases, List<Issue> issues) {
        Map<String, Issue> uniqueKeyMap = new HashMap<>(issues.size());
        for (Issue issue : issues) {
            uniqueKeyMap.put(issue.getKey(), issue);
            String testCaseUniqueId = issue.getFields().getTestCaseUniqueId();
            if (testCaseUniqueId != null) {
                uniqueKeyMap.put(testCaseUniqueId, issue);
            }
        }

        for (TestCase testCase : resultTestCases) {
            String testCaseKey = testCase.getKey();
            String testCaseName = testCase.getName();
            // case when test case can be matched by id
            if (testCaseKey != null && uniqueKeyMap.containsKey(testCaseKey)) {
                testCase.setId(uniqueKeyMap.get(testCaseKey).getId());
                continue;
            }
            // if no exact match by id was found, trying to match by exact name
            if (testCaseKey == null && testCaseName != null) {
                for (Issue issue : issues) {
                    if (issue.getFields().getSummary().equalsIgnoreCase(testCaseName)) {
                        testCase.setId(issue.getId());
                        testCase.setKey(issue.getKey());
                        continue;
                    }
                }
            }
            // if no exact match by id or by name, creating new one
            if (testCaseKey != null) {
                log(format("INFO: Key %s not found, new Test Case will be created", testCaseKey));
                testCase.setId(null);
                testCase.setKey(null);
                continue;
            }
        }
    }
}

