package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentHighLevelService;
import com.zjy.esdemo.service.StudentLowLevelService;
import com.zjy.esdemo.service.StudentService;
import com.zjy.esdemo.service.impl.EsHighLevelOpt;
import com.zjy.esdemo.service.impl.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    StudentService studentService;
    @Autowired
    StudentHighLevelService studentHighLevelService;
    @Autowired
    EsHighLevelOpt esHighLevelOpt;

    @Autowired
    StudentLowLevelService studentLowLevelService;


    @RequestMapping("initData")
    public String initData() {
        String initData = Utils.getJsonReader("initData.json");
        List<Student> studentList = JSON.parseArray(initData, Student.class);
        studentHighLevelService.bulkInsert("student_index", studentList);
        return "success";
    }

    // region starter
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
    // endregion

    // region highlevel
    @RequestMapping("createIndex")
    public String createIndex(String index) {
        studentHighLevelService.createIndex(index);
        return "success";
    }

    @RequestMapping("deleteIndex")
    public String deleteIndex(String index) {
        studentHighLevelService.deleteIndex(index);
        return "success";
    }

    @RequestMapping("insert")
    public String insert() {
        studentHighLevelService.insertTestDoc("student_index");
        return "success";
    }

    @RequestMapping("batchInsertDoc")
    public String batchInsertDoc() {
        studentHighLevelService.batchInsertTestDoc("student_index");
        return "success";
    }

    @RequestMapping("update")
    public String update(long id, int age) {
        studentHighLevelService.update("student_index", id, age);
        return "success";
    }

    @RequestMapping("delete")
    public String delete(String id) {
        studentHighLevelService.deleteDoc("student_index", id);
        return "success";
    }

    @RequestMapping("search")
    public List<Student> search() {
        return studentHighLevelService.search();
    }

    @RequestMapping("es")
    public Object es() {
        List<String> ids = new ArrayList<>();
        ids.add("2");
        ids.add("6");
        ids.add("8");
        return studentHighLevelService.getList("student_index", ids);
    }

    @RequestMapping("createEsTemplate")
    public String createEsTemplate() {
        return esHighLevelOpt.createEsTemplate("student_search");
    }

    @RequestMapping("getEsTemplate")
    public String getEsTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        return esHighLevelOpt.getEsTemplate("student_search", map);
    }

    @RequestMapping("searchByTemplate")
    public String searchByTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        List<Map<String, Object>> maps = esHighLevelOpt.useEsTemplate("student_index", "student_search", map);
        return JSON.toJSONString(maps);
    }


    // endregion

    // region lowlevel
    @RequestMapping("queryList")
    public String queryList() {
        return studentLowLevelService.queryList();
    }
    // endregion
}
