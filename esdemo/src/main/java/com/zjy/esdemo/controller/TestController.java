package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
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
    public String save(Student bean) {
        studentService.save(bean);
        return "success";
    }

    @RequestMapping("deleteAll")
    public String deleteAll() {
        studentService.deleteAll();
        return "success";
    }

    @RequestMapping("insert")
    public String insert() {
        studentService.insertDoc("student_index");
        return "success";
    }

    @RequestMapping("update")
    public String update(long id, int age) {
        studentService.update("student_index", id, age);
        return "success";
    }

    @RequestMapping("delete")
    public String delete(String id) {
        studentService.deleteDoc("student_index", id);
        return "success";
    }

    @RequestMapping("batchInsertDoc")
    public String batchInsertDoc() {
        studentService.batchInsertDoc("student_index");
        return "success";
    }

    @RequestMapping("search")
    public List<Student> search() {
        return studentService.search();
    }

    @RequestMapping("createIndex")
    public String createIndex(String index) {
        studentService.createIndex(index);
        return "success";
    }

    @RequestMapping("deleteIndex")
    public String deleteIndex(String index) {
        studentService.deleteIndex(index);
        return "success";
    }
}
