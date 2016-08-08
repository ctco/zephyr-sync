package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.beans.Metafield;

import java.util.List;

public class Project {

    private String id;
    private String key;
    private List<Metafield> versions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Metafield> getVersions() {
        return versions;
    }

    public void setVersions(List<Metafield> versions) {
        this.versions = versions;
    }
}
