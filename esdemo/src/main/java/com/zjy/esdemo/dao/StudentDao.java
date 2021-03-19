package com.zjy.esdemo.dao;

import com.zjy.esdemo.po.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StudentDao extends ElasticsearchRepository<Student, Long> {
    List<Student> findByName(String name);

//    List<Student> findByNameOrDesc(String text);
}
