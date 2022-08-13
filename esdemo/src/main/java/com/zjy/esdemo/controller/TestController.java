package com.zjy.esdemo.controller;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentHighLevelService;
import com.zjy.esdemo.service.StudentLowLevelService;
import com.zjy.esdemo.service.StudentService;
import com.zjy.esdemo.service.impl.Constants;
import com.zjy.esdemo.service.impl.EsHighLevelOpt;
import com.zjy.esdemo.service.impl.StudentServiceImpl;
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
        String initData = Utils.getJsonByTemplate("initData.json");
        List<Student> studentList = JSON.parseArray(initData, Student.class);
        studentHighLevelService.bulkInsert(Constants.STUDENT_INDEX, studentList);
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
    public String save() {
        Student bean = new Student();
        bean.setStudentId(10L);
        bean.setName("曾军毅");
        bean.setAge(43);
        studentService.save(bean);
        return "success";
    }

    @RequestMapping("deleteAll")
    public String deleteAll() {
        studentService.deleteAll();
        return "success";
    }

    @RequestMapping("test")
    public String test() {
//        List<Student> list = ((StudentServiceImpl) studentService).contain("王二丫");
//        List<Student> list = ((StudentServiceImpl) studentService).contain("王二丫", PageRequest.of(0, 5));
        List<Student> list = ((StudentServiceImpl) studentService).bool("王二丫", "jkl", 25);
        return JSON.toJSONString(list);
    }
    // endregion

    // region highlevel
    @RequestMapping("createIndex")
    public boolean createIndex(String index) {
        return studentHighLevelService.createIndex(index);
//        return studentService.createIndex();
    }

    @RequestMapping("deleteIndex")
    public boolean deleteIndex(String index) {
        return studentHighLevelService.deleteIndex(index);
    }

    @RequestMapping("insert")
    public String insert() {
        studentHighLevelService.insertTestDoc(Constants.STUDENT_INDEX);
        return "success";
    }

    @RequestMapping("batchInsertDoc")
    public String batchInsertDoc() {
        studentHighLevelService.batchInsertTestDoc(Constants.STUDENT_INDEX);
        return "success";
    }

    @RequestMapping("update")
    public String update(long id, int age) {
        studentHighLevelService.update(Constants.STUDENT_INDEX, id, age);
        return "success";
    }

    @RequestMapping("delete")
    public String delete(String id) {
        studentHighLevelService.deleteDoc(Constants.STUDENT_INDEX, id);
        return "success";
    }

    @RequestMapping("deleteByQuery")
    public String deleteByQuery() {
        esHighLevelOpt.deleteByQuery();
        return "success";
    }

    @RequestMapping("get")
    public Student get() {
        return studentHighLevelService.get(5L);
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
        return studentHighLevelService.getList(Constants.STUDENT_INDEX, ids);
    }

    @RequestMapping("createEsTemplate")
    public String createEsTemplate() {
        return esHighLevelOpt.createEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE);
    }

    @RequestMapping("deleteEsTemplate")
    public String deleteEsTemplate() {
        return esHighLevelOpt.deleteEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE);
    }

    @RequestMapping("getEsTemplate")
    public String getEsTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        return esHighLevelOpt.getEsTemplate(Constants.STUDENT_SEARCH_TEMPLATE, map);
    }

    @RequestMapping("searchByTemplate")
    public String searchByTemplate() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "王二丫");
        map.put("gt", 20);
        map.put("lt", 40);
        List<Map<String, Object>> maps = esHighLevelOpt.useEsTemplate(Constants.STUDENT_INDEX, Constants.STUDENT_SEARCH_TEMPLATE, map);
        return JSON.toJSONString(maps);
    }

    @RequestMapping("suggest")
    public List<String> suggest(String keyword) {
        return esHighLevelOpt.suggest(Constants.STUDENT_INDEX, keyword);
    }
    // endregion

    // region lowlevel
    @RequestMapping("queryList")
    public String queryList() {
        return studentLowLevelService.queryList();
    }
    // endregion
}
