package com.zjy.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.mp.entity.MyUser;

/**
 * TODO
 */
public interface MyUserDao extends BaseMapper<MyUser> {
    MyUser selectByName(String name);
}
