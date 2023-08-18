package com.zjy.esdemo.test;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ValueLocation implements Serializable {
    private String text;

    private Integer start;

    private Integer end;

    public ValueLocation(String text, Integer start, Integer end) {
        this.text = text;
        this.start = start;
        this.end = end;
    }
}