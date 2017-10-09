package lv.ctco.zephyr;

import lv.ctco.zephyr.enums.ConfigProperty;

import java.util.*;

public class Config {

    public interface Loader {
        void execute(Config config);
    }

    public static final Loader EMPTY_LOADER = new Loader() {
        public void execute(Config config) {
            //Do nothing
        }
    };

    final Map<ConfigProperty, String> properties;

    public Config(Loader loader) {
        this();
        loader.execute(this);
        new AutodetectReportType().execute(this);
        applyDefaults();
        validateMandatoryAttributes();
    }

    Config() {
        properties = new HashMap<ConfigProperty, String>();
    }

    public String getValue(ConfigProperty property) {
        return properties.get(property);
    }

    public void setValue(ConfigProperty property, String value) {
        properties.put(property, value);
    }

    public void setValue(ConfigProperty property, Boolean value) {
        properties.put(property, value == null ? null : value.toString());
    }

    public void applyDefault(ConfigProperty property, String value) {
        if (properties.get(property) == null) {
            setValue(property, value);
        }
    }

    void applyDefaults() {
        for (ConfigProperty property : ConfigProperty.values()) {
            if (property.getDefaultValue() != null) {
                applyDefault(property, property.getDefaultValue());
            }
        }
    }

    void validateMandatoryAttributes() {
        List<String> mandatoryProperties = new ArrayList<String>();
        for (ConfigProperty property : ConfigProperty.values()) {
            if (property.isMandatory() && properties.get(property) == null) {
                mandatoryProperties.add(property.getPropertyName());
            }
        }
        if (!mandatoryProperties.isEmpty()) {
            throw new ZephyrSyncException("The following properties are not passed: " + mandatoryProperties);
        }
    }
}