package lv.ctco.zephyr.beans.zapi;

import java.util.List;

public class ExecutionResponse {

    private int totalCount;
    private List<Execution> executions;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions = executions;
    }
}
