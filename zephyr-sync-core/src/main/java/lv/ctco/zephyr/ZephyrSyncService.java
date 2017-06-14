package lv.ctco.zephyr;

import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.util.CustomPropertyNamingStrategy;
import lv.ctco.zephyr.util.ObjectTransformer;
import lv.ctco.zephyr.service.*;

import java.io.IOException;
import java.util.List;

public class ZephyrSyncService {

    private AuthService authService;
    private MetaInfoRetrievalService metaInfoRetrievalService;
    private TestCaseResolutionService testCaseResolutionService;
    private JiraService jiraService;
    private ZephyrService zephyrService;

    public ZephyrSyncService(Config config) {
        ObjectTransformer.setPropertyNamingStrategy(new CustomPropertyNamingStrategy(config));

        authService = new AuthService(config);
        metaInfoRetrievalService = new MetaInfoRetrievalService(config);
        testCaseResolutionService = new TestCaseResolutionService(config);
        jiraService = new JiraService(config);
        zephyrService = new ZephyrService(config);
    }

    public void execute() throws IOException {
        authService.authenticateInJira();

        MetaInfo metaInfo = metaInfoRetrievalService.retrieve();

        List<TestCase> testCases = testCaseResolutionService.resolveTestCases();
        List<Issue> issues = jiraService.getTestIssues();

        zephyrService.mapTestCasesToIssues(testCases, issues);

        for (TestCase testCase : testCases) {
            if (testCase.getId() == null) {
                jiraService.createTestIssue(testCase);
                zephyrService.addStepsToTestIssue(testCase);
                jiraService.linkToStory(testCase);
            }
        }

        zephyrService.linkExecutionsToTestCycle(metaInfo, testCases);
        // needed to update correcty status of test cases
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.print("Error not catched ! ");
        }
        zephyrService.updateExecutionStatuses(testCases);

    }
}
