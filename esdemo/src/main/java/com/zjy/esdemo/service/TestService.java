package com.zjy.esdemo.service;

import com.zjy.esdemo.po.TestPo;

import java.util.List;

public interface TestService {
    List<TestPo> findAll();

    void createIndex();
    void saveTest();

    void save(TestPo bean);

    List<TestPo> findByName(String text);

    List<TestPo> findByNameOrDesc(String name);

    void deleteAll();
}
