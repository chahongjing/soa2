package com.zjy.enums;

/**
 * TODO
 */
public enum WorkOrderType {
    DEFAULT(0, "默认"),
    APP(1, "app反馈工单"),
    AFTERSALE(2, "售后工单");

    private int value;
    private String name;

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    WorkOrderType(int value, String name){
        this.value = value;
        this.name = name;
    }
}
