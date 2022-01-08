package com.zjy.esdemo.service;

import com.zjy.esdemo.po.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    List<Student> findByName(String text);

    void saveTest();

    void save(Student bean);

    void deleteAll();

    void update(String index, long id, int age);

    void insertDoc(String index);

    void deleteDoc(String index, String id);

    void batchInsertDoc(String index);

    List<Student> search();

    void createIndex(String index);

    void deleteIndex(String index);
}
