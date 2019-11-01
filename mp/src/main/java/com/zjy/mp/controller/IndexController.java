package com.zjy.mp.controller;

import com.zjy.mp.entity.MyUser;
import com.zjy.mp.mapper.MyUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
@RestController
@RequestMapping("/")
public class IndexController {
    @Autowired
    private MyUserDao myUserDao;

    @GetMapping("index")
    public Map test() {
        Map<String, String> map = new HashMap<>();
        MyUser myUser = myUserDao.selectById(1);

        MyUser zjy = myUserDao.selectByName("zjy");

        return map;
    }
}
