package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.service.StudentLowLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class StudentLowLevelServiceImpl implements StudentLowLevelService {

    @Resource
    private EsLowLevelOpt esLowLevelOpt;

    @Override
    public String queryList() {
        return JSON.toJSONString(esLowLevelOpt.queryList());
    }
}
