package lv.ctco.zephyr.enums;

import lv.ctco.zephyr.ZephyrSyncException;

public enum ConfigProperty {
    USERNAME("username", true),
    PASSWORD("password", true),
    REPORT_TYPE("reportType", true),
    PROJECT_KEY("projectKey", true),
    RELEASE_VERSION("releaseVersion", true),
    TEST_CYCLE("testCycle", true),
    JIRA_URL("jiraUrl", true),
    REPORT_PATH("reportPath", true),
    ORDERED_STEPS("orderedSteps", false, "false"),
    FORCE_STORY_LINK("forceStoryLink", false, "true"),
    TEST_CASE_UNIQUE_ID("testCaseUniqueId", false),
    GENERATE_TEST_CASE_UNIQUE_ID("generateTestCaseUniqueId", false, "false"),
    SEVERITY("severityAttribute", false),
    AUTO_CREATE_TEST_CYCLE("autoCreateTestCycle", false, "true"),
    ASSIGNEE("assignee", false)
    ;

    private String propertyName;
    private boolean mandatory;
    private String defaultValue;

    ConfigProperty(String propertyName, boolean mandatory) {
        this.propertyName = propertyName;
        this.mandatory = mandatory;
    }

    ConfigProperty(String propertyName, boolean mandatory, String defaultValue) {
        this(propertyName, mandatory);
        this.defaultValue = defaultValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static ConfigProperty findByName(String name) {
        for (ConfigProperty property : values()) {
            if (property.getPropertyName().equalsIgnoreCase(name)) {
                return property;
            }
        }
        throw new ZephyrSyncException("Unsupported parameter is passed " + name);
    }
}