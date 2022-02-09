package com.zjy.mvc.dao;

import com.zjy.mvc.common.sql.SqlLog;
import com.zjy.mvc.po.UserInfo;

import java.util.List;

public interface UserInfoDao {
    UserInfo get(String id);
    @SqlLog
    List<UserInfo> getList(String id);
}
