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

    public void setProperty(ConfigProperty property, String value) {
        properties.put(property, value);
    }

    public void applyDefault(ConfigProperty property, String value) {
        if (properties.get(property) == null) {
            setProperty(property, value);
        }
    }

    void applyDefaults() {
        for (ConfigProperty property : ConfigProperty.values()) {
            if (properties.get(property) == null && property.getDefaultValue() != null) {
                setProperty(property, property.getDefaultValue());
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

    public String getValue(ConfigProperty property) {
        String value = properties.get(property);
        if (value != null) {
            return value.trim();
        }
        if (property.getDefaultValue() != null) {
            return property.getDefaultValue();
        }
        throw new ZephyrSyncException("Property " + property.name() + " is not found in the config file!");
    }
}