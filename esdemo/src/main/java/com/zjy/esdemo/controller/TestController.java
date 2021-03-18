package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.TestPo;
import com.zjy.esdemo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/testes")
public class TestController {
    @Autowired
    TestService testService;

    @RequestMapping("findAll")
    public String findAll() {
        String s = JSON.toJSONString(testService.findAll());
        return s;
    }

    @RequestMapping("saveTest")
    public String saveTest() {
        testService.saveTest();

        return "success";
    }

    @RequestMapping("save")
    public void save(TestPo bean) {
        testService.save(bean);
    }

    @RequestMapping("deleteAll")
    void deleteAll() {
        testService.deleteAll();
    }

    @RequestMapping("createIndex")
    void createIndex() {
        testService.createIndex();
    }

    @RequestMapping("findByName")
    public List<TestPo> findByName(String name) {
        return testService.findByName(name);
    }

    @RequestMapping("findByNameOrDesc")
    public List<TestPo> findByNameOrDesc(String text) {
        return testService.findByNameOrDesc(text);
    }
}
