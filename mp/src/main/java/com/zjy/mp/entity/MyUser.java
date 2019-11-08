package com.zjy.mp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjy.mp.enums.Sex;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO
 */
@Getter
@Setter
@TableName("myuser")
public class MyUser {
    private String id;
    private String name;
    private Sex sex;
}
