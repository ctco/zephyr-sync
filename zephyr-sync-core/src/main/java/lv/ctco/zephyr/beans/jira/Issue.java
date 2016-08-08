package lv.ctco.zephyr.beans.jira;

public class Issue {
    private Integer id;
    private String key;
    private Fields fields;

    public Issue() {
        fields = new Fields();
    }

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

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }
}
