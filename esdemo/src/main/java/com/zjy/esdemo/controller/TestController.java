package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/testes")
public class TestController {
    @Autowired
    StudentService studentService;

    @RequestMapping("findAll")
    public String findAll() {
        return JSON.toJSONString(studentService.findAll());
    }

    @RequestMapping("findByName")
    public List<Student> findByName(String name) {
        return studentService.findByName(name);
    }

    @RequestMapping("saveTest")
    public String saveTest() {
        studentService.saveTest();
        return "success";
    }

    @RequestMapping("save")
    public void save(Student bean) {
        studentService.save(bean);
    }
}
