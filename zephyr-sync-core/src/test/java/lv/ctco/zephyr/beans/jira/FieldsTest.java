package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.Config;
import lv.ctco.zephyr.TestConfigLoader;
import lv.ctco.zephyr.beans.Metafield;
import lv.ctco.zephyr.util.CustomPropertyNamingStrategy;
import lv.ctco.zephyr.util.ObjectTransformer;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class FieldsTest {

    @Before
    public void setUp() throws Exception {
        ObjectTransformer.setPropertyNamingStrategy(new CustomPropertyNamingStrategy(new Config(new TestConfigLoader())));
    }

    @Test
    public void testSerializeToJson() throws Exception {
        Fields fields = new Fields();
        fields.setTestCaseUniqueId("env");
        Metafield severity = new Metafield();
        severity.setName("Low");
        fields.setSeverity(severity);

        String json = ObjectTransformer.serialize(fields);
        System.out.println(json);
        assertThat(json, CoreMatchers.containsString("\"environment\":\"env\""));
        assertThat(json, CoreMatchers.containsString("\"customfield_10067\":{\"name\":\"Low\"}"));
    }
}