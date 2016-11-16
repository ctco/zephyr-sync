package lv.ctco.zephyr.beans;

import lv.ctco.zephyr.enums.TestLevel;
import lv.ctco.zephyr.enums.TestStatus;

import java.util.List;

public class TestCase {

    private Integer id;
    private String key;
    private String uniqueId;
    private String name;
    private List<String> storyKeys;
    private List<String> labels;
    private List<TestStep> steps;
    private TestStatus status = TestStatus.NOT_EXECUTED;
    private TestLevel severity;
    private TestLevel priority = TestLevel.MEDIUM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStoryKeys() {
        return storyKeys;
    }

    public void setStoryKeys(List<String> storyKeys) {
        this.storyKeys = storyKeys;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TestStep> steps) {
        this.steps = steps;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public TestLevel getSeverity() {
        return severity;
    }

    public void setSeverity(TestLevel severity) {
        this.severity = severity;
    }

    public TestLevel getPriority() {
        return priority;
    }

    public void setPriority(TestLevel priority) {
        this.priority = priority;
    }
}