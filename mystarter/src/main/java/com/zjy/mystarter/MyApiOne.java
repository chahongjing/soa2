package com.zjy.mystarter;

import com.zjy.api.TestApi;

/**
 * TODO
 */
public class MyApiOne implements TestApi {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String test() {
        return null;
    }
}
