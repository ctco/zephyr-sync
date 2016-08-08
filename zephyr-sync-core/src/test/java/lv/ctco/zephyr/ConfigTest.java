package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class ConfigTest {

    private Config config;
    private Map<ConfigProperty, String> properties;

    @Before
    public void setUp() throws Exception {
        config = new Config();
        properties = config.properties;
    }

    @Test
    public void testApplyDefaults() throws Exception {
        config.applyDefaults();
        assertThat(properties.size(), is(not(0)));
        for (Map.Entry<ConfigProperty, String> entry : properties.entrySet()) {
            assertThat(entry.getValue(), is(not(nullValue())));
        }
    }

    @Test
    public void testValidateMandatoryAttributes() throws Exception {
        for (ConfigProperty property : ConfigProperty.values()) {
            properties.put(property, "aaa");
        }
        config.validateMandatoryAttributes();
    }

    @Test(expected = ZephyrSyncException.class)
    public void testValidateMandatoryAttributes_SomethingMissing() throws Exception {
        config.validateMandatoryAttributes();
    }
}