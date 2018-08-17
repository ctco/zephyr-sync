package lv.ctco.zephyr.service;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.ZephyrSyncException;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.beans.jira.IssueLink;
import lv.ctco.zephyr.beans.jira.SearchResponse;
import lv.ctco.zephyr.transformer.TestCaseToIssueTransformer;
import lv.ctco.zephyr.util.HttpUtils;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.List;

import static lv.ctco.zephyr.enums.ConfigProperty.FORCE_STORY_LINK;
import static lv.ctco.zephyr.enums.ConfigProperty.LINK_TYPE;
import static lv.ctco.zephyr.enums.ConfigProperty.PROJECT_KEY;
import static lv.ctco.zephyr.util.HttpUtils.*;
import static lv.ctco.zephyr.util.Utils.log;
import static lv.ctco.zephyr.util.Utils.readInputStream;
import static java.lang.String.format;

public class JiraService {

    private static final int TOP = 20;

    private Config config;

    public JiraService(Config config) {
        this.config = config;
    }

    public List<Issue> getTestIssues() throws IOException {
        int skip = 0;
        log("Fetching JIRA Test issues for the project");
        String search = "project='" + config.getValue(PROJECT_KEY) + "'%20and%20issueType=Test";
        SearchResponse searchResults = searchInJQL(search, skip);
        if (searchResults == null || searchResults.getIssues() == null) {
            throw new ZephyrSyncException("Unable to fetch JIRA test issues");
        }

        List<Issue> issues = searchResults.getIssues();

        int totalCount = searchResults.getTotal();
        if (totalCount > TOP) {
            while (issues.size() != totalCount) {
                skip += TOP;
                issues.addAll(searchInJQL(search, skip).getIssues());
            }
        }
        log(format("Retrieved %s Test issues\n", issues.size()));
        return issues;
    }

    SearchResponse searchInJQL(String search, int skip) throws IOException {
        String response = getAndReturnBody(config, "api/2/search?jql=" + search + "&maxResults=" + TOP + "&startAt=" + skip);
        return ObjectTransformer.deserialize(response, SearchResponse.class);
    }

    public void createTestIssue(TestCase testCase) throws IOException {
        log("INFO: Creating JIRA Test item with Name: \"" + testCase.getName() + "\".");
        Issue issue = TestCaseToIssueTransformer.transform(config, testCase);

        HttpResponse response = post(config, "api/2/issue", issue);
        ensureResponse(response, 201, "ERROR: Could not create JIRA Test item");

        String responseBody = readInputStream(response.getEntity().getContent());
        Metafield result = ObjectTransformer.deserialize(responseBody, Metafield.class);
        if (result != null) {
            testCase.setId(Integer.valueOf(result.getId()));
            testCase.setKey(result.getKey());
        }
        log("INFO: Created. JIRA Test item Id is: [" + testCase.getKey() + "].");
    }

    public void linkToStory(TestCase testCase) throws IOException {
        List<String> storyKeys = testCase.getStoryKeys();
        if (Boolean.valueOf(config.getValue(FORCE_STORY_LINK))) {
            if (storyKeys == null || storyKeys.isEmpty()) {
                throw new ZephyrSyncException("Linking Test issues to Story is mandatory, please check if Story marker exists in " + testCase.getKey());
            }
        }
        if (storyKeys == null) return;

        log("Linking Test issue " + testCase.getKey() + " to Stories " + testCase.getStoryKeys());
        for (String storyKey : storyKeys) {
            HttpResponse response = post(config, "api/2/issueLink", new IssueLink(testCase.getKey(), storyKey.toUpperCase(), config.getValue(LINK_TYPE)));
            ensureResponse(response, 201, "Could not link Test issue: " + testCase.getId() + " to Story " + storyKey + ". " +
                    "Please check if Story issue exists and is valid");
        }
    }

}
