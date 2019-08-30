package com.zjy.service;

import com.zjy.dao.MyUserMapper;
import com.zjy.po.MyUser;
import com.zjy.po.MyUserCondition;
import com.zjy.service.common.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
public class MyUserServiceImpl extends BaseServiceImpl<MyUserMapper, MyUser, MyUserCondition> implements MyUserService {
}
