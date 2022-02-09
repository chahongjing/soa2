package com.zjy.mvc.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zjy.mvc.common.sql.SqlLog;
import com.zjy.mvc.dao.UserInfoDao;
import com.zjy.mvc.common.multiDataSource.DBSource;
import com.zjy.mvc.po.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserInfoDao userInfoDao;

    @DBSource
    public UserInfo getFromMaster() {
        PageHelper.startPage(1, 1);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoDao.getList("1"));
        UserInfo userInfo = null;
        if(pageInfo.getTotal() > 0) {
            userInfo = pageInfo.getList().get(0);
            log.info("user master: {}", userInfo.getUserName());
        }
        return userInfo;
    }

    @DBSource("slave")
    public UserInfo getFromSlave() {
        UserInfo userInfo = userInfoDao.get("2");
        log.info("user slave: {}", userInfo.getUserName());
        return userInfo;
    }
}
