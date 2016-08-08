package lv.ctco.zephyr.beans;

import java.util.List;

public class TestStep {

    private String description;
    private List<TestStep> steps = null;

    public TestStep() {
    }

    public TestStep(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TestStep> steps) {
        this.steps = steps;
    }
}