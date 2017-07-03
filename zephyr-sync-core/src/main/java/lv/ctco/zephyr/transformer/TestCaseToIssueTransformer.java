package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.beans.jira.Fields;
import lv.ctco.zephyr.beans.jira.Issue;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.enums.IssueType;

import java.util.ArrayList;
import java.util.List;

public class TestCaseToIssueTransformer {

    public static Issue transform(Config config, TestCase testCase) {
        Issue issue = new Issue();
        Fields fields = issue.getFields();
        fields.setSummary(testCase.getName());
        fields.setDescription(testCase.getDescription());
        fields.setTestCaseUniqueId(testCase.getUniqueId());

        List<String> labels = new ArrayList<>();
        labels.add("Automation");
        List<String> testLabels = testCase.getLabels();
        if (testLabels != null && testLabels.size() > 0) {
            labels.addAll(testLabels);
        }
        fields.setLabels(labels.toArray(new String[labels.size()]));

        Metafield project = new Metafield();
        project.setKey(config.getValue(ConfigProperty.PROJECT_KEY));
        fields.setProject(project);

        Metafield issueType = new Metafield();
        issueType.setName(IssueType.TEST.getName());
        fields.setIssuetype(issueType);

        Metafield assignee = new Metafield();
        assignee.setName("");
        fields.setAssignee(assignee);

        if (testCase.getPriority() != null) {
            Metafield priority = new Metafield();
            priority.setName(testCase.getPriority().getName());
            fields.setPriority(priority);
        }

        if (testCase.getSeverity() != null) {
            Metafield severity = new Metafield();
            severity.setId(testCase.getSeverity().getIndex().toString());
            fields.setSeverity(severity);
        }

        List<Metafield> versions = new ArrayList<Metafield>(1);
        Metafield version = new Metafield();
        version.setName(config.getValue(ConfigProperty.RELEASE_VERSION));
        versions.add(version);
        fields.setVersions(versions);
        return issue;
    }
}
