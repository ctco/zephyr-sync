package lv.ctco.zephyr.beans.jira;

import java.util.stream.Stream;

public enum IssueLinkDirection {
    inward, outward;

    public static IssueLinkDirection ofValue(String value) {
        return Stream.of(values())
                .filter(dir -> dir.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Issue Link Direction '" + value + "' is not supported."));
    }
}
