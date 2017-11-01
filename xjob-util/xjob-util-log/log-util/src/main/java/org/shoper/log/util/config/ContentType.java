package org.shoper.log.util.config;

/**
 * Content Type.
 * Now, support  character string/json.
 */
public enum ContentType {
    STR(0, "STR"), JSON(1, "JSON");

    ContentType(int value, String name) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    private String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private int getValue() {
        return value;
    }

    private void setValue(short value) {
        this.value = value;
    }

}
