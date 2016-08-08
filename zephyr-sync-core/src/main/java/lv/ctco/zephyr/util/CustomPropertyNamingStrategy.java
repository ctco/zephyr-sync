package lv.ctco.zephyr.util;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import lv.ctco.zephyr.Config;

public class CustomPropertyNamingStrategy extends PropertyNamingStrategy {

    private Config config;

    public CustomPropertyNamingStrategy(Config config) {
        this.config = config;
    }

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return nameForAnnotated(field, defaultName);
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return nameForAnnotated(method, defaultName);
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return nameForAnnotated(method, defaultName);
    }

    String nameForAnnotated(AnnotatedMember member, String defaultName) {
        ConfigBasedJsonProperty annotation = member.getAnnotation(ConfigBasedJsonProperty.class);
        if (annotation == null) {
            return defaultName;
        }
        return config.getValue(annotation.value());
    }
}
