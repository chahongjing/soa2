package com.zjy.mp.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * TODO
 */
public enum Sex implements IEnum<Integer> {
    // 女
    Female(0, "女"),
    Male(1, "男");

    private int value;
    private String name;

    Sex(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
