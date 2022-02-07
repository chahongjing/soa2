package com.zjy.mvc.enums;

import com.zjy.mvc.common.IBaseEnum;
import lombok.Getter;

@Getter
public enum DownTaskStatus implements IBaseEnum {
    CREATED(0, "已创建"),
    STARTED(1, "处理中"),
    FINISHED(2, "已完成"),
    ERROR(9, "错误");

    private int value;
    private String name;

    DownTaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
