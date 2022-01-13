package com.zjy.esdemo.service.impl;

import com.zjy.esdemo.dao.StudentDao;
import com.zjy.esdemo.po.Address;
import com.zjy.esdemo.po.Interest;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Resource
    private EsUtils esUtils;

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
        Address address = new Address();
        address.setProvince("湖北省");
        address.setCity("武汉市");
        address.setArea("高新区");

        List<Interest> list = new ArrayList<>();
        Interest interest = new Interest();
        interest.setCode("1");
        interest.setName("看书");
        list.add(interest);
        interest = new Interest();
        interest.setCode("2");
        interest.setName("打游戏");
        list.add(interest);

        Student student = new Student(1L, "刘伯", 21, scores, new Date(2022 - 1900, Calendar.JANUARY, 2, 3, 4, 5));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(2L, "刘思想", 35, scores, new Date(2022 - 1900, Calendar.JANUARY, 3, 7, 5, 35));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(3L, "王皮皮", 45, scores, new Date(2022 - 1900, Calendar.JANUARY, 4, 8, 24, 45));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(4L, "王二丫", 23, scores, new Date(2022 - 1900, Calendar.JANUARY, 5, 19, 54, 32));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);

        student = new Student(5L, "王铁蛋", 51, scores, new Date(2022 - 1900, Calendar.JANUARY, 6, 12, 33, 18));
        student.setAddress(address);
        student.setInterests(list);
        studentDao.save(student);
    }

    @Override
    public void save(Student bean) {
        studentDao.save(bean);
    }

    @Override
    public void deleteAll() {
        studentDao.deleteAll();
    }

    @Override
    public void createIndex(String index) {
        try {
            esUtils.createIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIndex(String index) {
        try {
            esUtils.deleteIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String index, long id, int age) {
        esUtils.updateDoc2(index, String.valueOf(id), age);
    }

    @Override
    public void insertDoc(String index) {
        Student student = new Student(6L, "黄金", 65, new ArrayList<>(), new Date(2022 - 1900, Calendar.FEBRUARY, 5, 23, 52, 18));

        List<Interest> list = new ArrayList<>();
        Interest interest = new Interest();
        interest.setCode("1");
        interest.setName("看电影");
        list.add(interest);
        interest = new Interest();
        interest.setCode("2");
        interest.setName("打游戏");
        list.add(interest);

        Address address = new Address();
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setArea("海淀区");
        student.setAddress(address);
        student.setInterests(list);
        esUtils.insertDoc(index, student.getStudentId().toString(), student);
    }

    @Override
    public void deleteDoc(String index, String id) {
        esUtils.deleteDoc(index, id);
    }

    @Override
    public void batchInsertDoc(String index) {
        List<Student> list = new ArrayList<>();
        Address address = new Address();
        address.setProvince("湖北省");
        address.setCity("襄阳市");
        address.setArea("南漳县");
        List<Interest> interests = new ArrayList<>();
        Interest interest = new Interest();
        interest.setCode("1");
        interest.setName("写代码");
        interests.add(interest);
        interest = new Interest();
        interest.setCode("2");
        interest.setName("刷视频");
        interests.add(interest);

        Student student = new Student(7L, "黄金", 65, new ArrayList<>(), new Date(2022 - 1900, Calendar.APRIL, 5, 23, 52, 18));
        student.setAddress(address);
        student.setInterests(interests);
        list.add(student);
        student = new Student(8L, "白银", 34, new ArrayList<>(), new Date(2022 - 1900, Calendar.APRIL, 27, 6, 29, 41));
        student.setAddress(address);
        student.setInterests(interests);
        list.add(student);
        esUtils.bulkInsert(index, list);
//        esUtils.bulkIndex(index, list);
    }


    //查询
    public List<Student> search() {
        return esUtils.mulitSearch();
    }
}
