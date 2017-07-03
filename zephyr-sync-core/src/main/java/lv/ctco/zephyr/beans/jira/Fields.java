package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.enums.ConfigProperty;
import lv.ctco.zephyr.util.ConfigBasedJsonProperty;

import java.util.List;

public class Fields {
    private String summary;
    private String description;
    private String testCaseUniqueId;
    private Metafield project;
    private Metafield assignee;
    private Metafield issuetype;
    private Metafield priority;
    private Metafield severity;
    private List<Metafield> versions;
    private String[] labels;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ConfigBasedJsonProperty(ConfigProperty.TEST_CASE_UNIQUE_ID)
    public String getTestCaseUniqueId() {
        return testCaseUniqueId;
    }

    @ConfigBasedJsonProperty(ConfigProperty.TEST_CASE_UNIQUE_ID)
    public void setTestCaseUniqueId(String testCaseUniqueId) {
        this.testCaseUniqueId = testCaseUniqueId;
    }

    public Metafield getProject() {
        return project;
    }

    public void setProject(Metafield project) {
        this.project = project;
    }

    public Metafield getAssignee() {
        return assignee;
    }

    public void setAssignee(Metafield assignee) {
        this.assignee = assignee;
    }

    public Metafield getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Metafield issuetype) {
        this.issuetype = issuetype;
    }

    public List<Metafield> getVersions() {
        return versions;
    }

    public void setVersions(List<Metafield> versions) {
        this.versions = versions;
    }

    public Metafield getPriority() {
        return priority;
    }

    public void setPriority(Metafield priority) {
        this.priority = priority;
    }

    @ConfigBasedJsonProperty(ConfigProperty.SEVERITY)
    public Metafield getSeverity() {
        return severity;
    }

    @ConfigBasedJsonProperty(ConfigProperty.SEVERITY)
    public void setSeverity(Metafield severity) {
        this.severity = severity;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }
}
