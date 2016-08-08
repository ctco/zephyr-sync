package lv.ctco.zephyr.util;

import lv.ctco.zephyr.enums.ConfigProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigBasedJsonProperty {
    ConfigProperty value();
}
