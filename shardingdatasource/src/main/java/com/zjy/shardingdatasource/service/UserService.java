package com.zjy.shardingdatasource.service;

import com.zjy.shardingdatasource.model.UserInfo;

import java.util.Collection;

public interface UserService {

    UserInfo getById(Long id);

    UserInfo getByIdList(Collection<Long> idList);

    UserInfo getLog(Long id);
}
