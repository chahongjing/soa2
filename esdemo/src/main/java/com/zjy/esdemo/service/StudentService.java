package com.zjy.esdemo.service;

import com.zjy.esdemo.po.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    List<Student> findByName(String text);

    void saveTest();

    void save(Student bean);

    void deleteAll();

    void deleteIndex();

    List<Student> search();
}
