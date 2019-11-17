package com.zjy.multidatasource.controller;

import com.zjy.dao.MyUserMapper;
import com.zjy.dao.WorkOrderMapper;
import com.zjy.po.MyUser;
import com.zjy.po.TestTable;
import com.zjy.slavedao.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 */
@RestController
public class IndexController {
    @Autowired
    private MyUserMapper myUserMapper;

    @Autowired
    private TestMapper testMapper;

    @GetMapping("index")
    public String index() {
        MyUser myUser = myUserMapper.selectById(1L);
        TestTable testTable = testMapper.selectById(1L);
        return "OK";
    }
}
