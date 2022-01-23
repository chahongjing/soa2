package com.zjy.esdemo.service;

import com.zjy.esdemo.po.Student;

import java.util.List;

public interface StudentHighLevelService {

    boolean createIndex(String index);

    boolean deleteIndex(String index);

    void insert(String index, Student student);

    void bulkInsert(String index, List<Student> studentList);

    void insertTestDoc(String index);

    void batchInsertTestDoc(String index);

    void update(String index, long id, int age);

    void deleteDoc(String index, String id);

    Student get(Long id);

    List<Student> search();

    List<Student> getList(String index, List<String> memberIds);
}
