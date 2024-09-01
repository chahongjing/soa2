package com.zjy.shardingdatasource.service.impl;

import com.zjy.shardingdatasource.mapper.UserMapper;
import com.zjy.shardingdatasource.model.UserInfo;
import com.zjy.shardingdatasource.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public UserInfo getById(Long id) {
        return userMapper.getById(id);
    }
    @Override
    public UserInfo getByIdList(Collection<Long> idList) {
        return userMapper.getByIdList(idList);
    }

    @Override
    public UserInfo getLog(Long id) {
        return userMapper.getLog(id);
    }
}
