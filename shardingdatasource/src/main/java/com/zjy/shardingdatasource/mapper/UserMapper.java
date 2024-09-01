package com.zjy.shardingdatasource.mapper;

import com.zjy.shardingdatasource.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface UserMapper {
    UserInfo getById(Long id);

    UserInfo getByIdList(Collection<Long> idList);

    UserInfo getLog(Long id);
}
