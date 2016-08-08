package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;

public class TestConfigLoader implements Config.Loader {

    public void execute(Config config) {
        for (ConfigProperty configProperty : ConfigProperty.values()) {
            config.setProperty(configProperty, configProperty.getDefaultValue() == null ? "test" : configProperty.getDefaultValue());
        }
    }
}
