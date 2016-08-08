package lv.ctco.zephyr.enums;

public enum TestLevel {
    LOW("Low", 10123),
    MEDIUM("Medium", 10122),
    HIGH("High", 10122),
    MAJOR("Major", 10122),
    MINOR("Minor", 10123),
    TRIVIAL("Trivial", 10124),
    BLOCKER("Blocker", 10120),
    CRITICAL("Critical", 10121);

    private String name;
    private Integer severityIndex;

    TestLevel(String name, Integer severityIndex) {
        this.name = name;
        this.severityIndex=severityIndex;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return severityIndex;
    }
}