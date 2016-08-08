package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CliConfigLoaderTest {

    @Test
    public void testExecute() throws Exception {
        Config config = new Config();
        Runner.CliConfigLoader configLoader = new Runner.CliConfigLoader(new String[] {"--projectKey=ABC"});
        configLoader.execute(config);
        Map<ConfigProperty, String> result = config.properties;
        assertThat(result.size(), is(1));
        assertThat(result.get(ConfigProperty.PROJECT_KEY), is("ABC"));
    }

    @Test(expected = ZephyrSyncException.class)
    public void testExecute_InvalidFormat() throws Exception {
        Config config = new Config();
        Runner.CliConfigLoader configLoader = new Runner.CliConfigLoader(new String[] {"projectKey=ABC"});
        configLoader.execute(config);
    }
}