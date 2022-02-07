package com.zjy.mvc.service;

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
        UserInfo userInfo = userInfoDao.get("1");
        log.info("user master: {}", userInfo.getUserName());
        return userInfo;
    }

    @DBSource("slave")
    public UserInfo getFromSlave() {
        UserInfo userInfo = userInfoDao.get("2");
        log.info("user slave: {}", userInfo.getUserName());
        return userInfo;
    }
}
