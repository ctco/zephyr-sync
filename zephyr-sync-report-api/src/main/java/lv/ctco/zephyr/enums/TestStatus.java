package lv.ctco.zephyr.enums;

public enum TestStatus {
    NOT_EXECUTED(-1),
    FAILED(2),
    PASSED(1);

    private int id;

    TestStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}