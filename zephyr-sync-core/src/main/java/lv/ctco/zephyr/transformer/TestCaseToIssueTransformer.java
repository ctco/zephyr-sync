package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.enums.IssueType;

import java.util.ArrayList;
import java.util.List;

public class TestCaseToIssueTransformer {


    public static Issue transform(Config config, TestCase testCase) {
        Issue issue = new Issue();
        if (config.getValue(ConfigProperty.GENERATE_TEST_CASE_UNIQUE_ID).equalsIgnoreCase("true")){
            issue.getFields().setTestCaseUniqueId(testCase.getUniqueId());
        }

        setIssueFieldsFromTestCaseAttributes(issue, testCase);
        setIssueFieldsFromConfig(issue, config);
        return issue;
    }

    public static void setIssueFieldsFromTestCaseAttributes(Issue issue, TestCase testCase){
        issue.getFields().setSummary(testCase.getName());
        issue.getFields().setDescription(testCase.getDescription());

        Metafield issueType = new Metafield();
        issueType.setName(IssueType.TEST.getName());
        issue.getFields().setIssuetype(issueType);

        if (testCase.getSeverity() != null) {
            Metafield severity = new Metafield();
            severity.setId(testCase.getSeverity().getIndex().toString());
            issue.getFields().setSeverity(severity);
        }

        if (testCase.getPriority() != null) {
            Metafield priority = new Metafield();
            priority.setName(testCase.getPriority().getName());
            issue.getFields().setPriority(priority);
        }

        List<String> labels = new ArrayList<>();
        labels.add("Automation");
        List<String> testLabels = testCase.getLabels();
        if (testLabels != null && testLabels.size() > 0) {
            labels.addAll(testLabels);
        }
        issue.getFields().setLabels(labels.toArray(new String[labels.size()]));
    }

    public static void setIssueFieldsFromConfig(Issue issue, Config config){
        for (ConfigProperty property : ConfigProperty.values()) {
            String value = config.getValue(property);
            Metafield metafield = new Metafield();
            if (value != null) {
                if (property.equals(ConfigProperty.ASSIGNEE)) {
                    metafield.setName(value);
                    issue.getFields().setAssignee(metafield);
                }
                if (property.equals(ConfigProperty.SEVERITY)) {
                    metafield.setName(value);
                    issue.getFields().setSeverity(metafield);
                }
                if (property.equals(ConfigProperty.RELEASE_VERSION)) {
                    metafield.setName(value);
                    List<Metafield> versions = new ArrayList<Metafield>(1);
                    versions.add(metafield);
                    issue.getFields().setVersions(versions);
                }
                if (property.equals(ConfigProperty.PROJECT_KEY)) {
                    metafield.setKey(value);
                    issue.getFields().setProject(metafield);
                }
            }
        }
    }
}
