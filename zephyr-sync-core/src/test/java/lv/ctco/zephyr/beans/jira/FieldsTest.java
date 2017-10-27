package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.beans.TestCase;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.enums.IssueType;
import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static lv.ctco.zephyr.transformer.TestCaseToIssueTransformer.setIssueFieldsFromConfig;
import static lv.ctco.zephyr.transformer.TestCaseToIssueTransformer.setIssueFieldsFromTestCaseAttributes;
import static org.junit.Assert.assertArrayEquals;


public class FieldsTest {

    @Test
    public void shouldPopulateTestCaseFieldsToJiraObject() throws Exception{
        TestCase testCase = createTestCase("testCaseName", "testCaseDescription", TestLevel.CRITICAL, TestLevel.LOW);
        Issue issue = createIssue();
        setIssueFieldsFromTestCaseAttributes(issue, testCase);
        assertEquals(testCase.getName(), issue.getFields().getSummary());
        assertEquals(testCase.getDescription(), issue.getFields().getDescription());
        assertArrayEquals(new String[]{"Automation"}, issue.getFields().getLabels());
        assertEquals(testCase.getPriority().getName(), issue.getFields().getPriority().getName());
        assertEquals(testCase.getSeverity().getIndex().toString(), issue.getFields().getSeverity().getId());
        assertEquals(IssueType.TEST.getName(), issue.getFields().getIssuetype().getName());
    }

    @Test
    public void shouldPopulateConfigFieldsToJiraObject()throws Exception{
        Issue issue = createIssue();
        Config config = createConfig("PRJ","Version 1.2.3", "employee", "Major");
        setIssueFieldsFromConfig(issue, config);
        assertEquals(config.getValue(ConfigProperty.PROJECT_KEY), issue.getFields().getProject().getKey());
        assertEquals(config.getValue(ConfigProperty.ASSIGNEE), issue.getFields().getAssignee().getName());
        assertEquals(config.getValue(ConfigProperty.SEVERITY), issue.getFields().getSeverity().getName());
    }

    @Test
    public void shouldSerializeJiraObjectToJson() throws Exception{
        TestCase testCase = createTestCase("testCaseName", "testCaseDescription", TestLevel.CRITICAL, TestLevel.LOW);
        Config config = createConfig("PRJ","Version 1.2.3", "employee", "Major");
        Issue issue = createIssue();
        setIssueFieldsFromConfig(issue, config);
        setIssueFieldsFromTestCaseAttributes(issue, testCase);
        String json = ObjectTransformer.serialize(issue.getFields());
        String expectedJson = "{\"summary\":\"testCaseName\",\"description\":\"testCaseDescription\",\"project\":{\"key\":\"PRJ\"},\"assignee\":{\"name\":\"employee\"},\"issuetype\":{\"name\":\"Test\"},\"priority\":{\"name\":\"Low\"},\"severity\":{\"id\":\"10121\"},\"versions\":[{\"name\":\"Version 1.2.3\"}],\"labels\":[\"Automation\"]}";
        assertEquals(expectedJson, json);
    }

    private Config createConfig (String cfgProjectKey, String cfgReleaseVersion, String cfgAssignee, String cfgSeverity){
        Config config = new Config();
        config.setValue(ConfigProperty.PROJECT_KEY, cfgProjectKey);
        config.setValue(ConfigProperty.RELEASE_VERSION, cfgReleaseVersion);
        config.setValue(ConfigProperty.ASSIGNEE, cfgAssignee);
        config.setValue(ConfigProperty.SEVERITY, cfgSeverity);
        return config;
    }

    private TestCase createTestCase (String tcName, String tcDescr, TestLevel tcSeverity, TestLevel tcPriority){
        TestCase testCase = new TestCase();
        testCase.setName(tcName);
        testCase.setDescription(tcDescr);
        testCase.setSeverity(tcSeverity);
        testCase.setPriority(tcPriority);
        return testCase;
    }

    private Issue createIssue (){
        Issue issue = new Issue();
        return issue;
    }
}