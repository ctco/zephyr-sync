package lv.ctco.zephyr.enums;

public enum IssueType {
    DEFECT("Defect"),
    TEST("Test");

    private String name;

    IssueType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}