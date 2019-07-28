package com.zjy.mystarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 */
@ConfigurationProperties(prefix = "my")
public class MyProperties {
    private static final String DEFAULT_NAME = "Lensen";
    private static final String DEFAULT_ENABELTWO = "false";

    private String name = DEFAULT_NAME;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String enabletwo = DEFAULT_ENABELTWO;

    public String getEnabletwo() {
        return enabletwo;
    }

    public void setEnabletwo(String enabletwo) {
        this.enabletwo = enabletwo;
    }
}
