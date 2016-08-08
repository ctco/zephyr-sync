package lv.ctco.zephyr.beans.testresult.cucumber;

public class Step {
    public Result result;
    public long line;
    public String name;
    public Match match;
    public String keyword;

    public Step() {
    }

    public Result getResult() {
        return result;
    }

    public long getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    public Match getMatch() {
        return match;
    }

    public String getKeyword() {
        return keyword;
    }
}
