package com.zjy.esdemo.service.impl;

import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> findAll() {
        Iterable<Student> all = studentDao.findAll();
        List<Student> list = new ArrayList<>();
        for (Student testPo : all) {
            list.add(testPo);
        }
        return list;
    }

    @Override
    public List<Student> findByName(String text) {
        return studentDao.findByName(text);
    }

    @Override
    public void saveTest() {
        List scores= new ArrayList<>();
        scores.add(67.2);
        scores.add(27.2);
        scores.add(56.2);

        studentDao.save(new Student(1L, "刘伯", 21, scores ));
        studentDao.save(new Student(2L, "刘思想", 35, scores ));
        studentDao.save(new Student(3L, "王皮皮", 45, scores ));
        studentDao.save(new Student(4L, "王二丫", 23, scores ));
        studentDao.save(new Student(5L, "王铁蛋", 51, scores ));
    }

    @Override
    public void save(Student bean) {
        studentDao.save(bean);
    }

}
