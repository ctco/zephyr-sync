package lv.ctco.zephyr.beans.zapi;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class CycleList {

    private HashMap<String, Cycle> cycleMap = new HashMap<String, Cycle>();

    private Integer recordsCount;

    @JsonAnySetter
    public void add(String key, Cycle user) {
        cycleMap.put(key, user);
    }

    @JsonAnyGetter
    public Map<String, Cycle> getCycleMap() {
        return cycleMap;
    }

    public Integer getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Integer recordsCount) {
        this.recordsCount = recordsCount;
    }
}
