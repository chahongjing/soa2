package com.zjy.esdemo.dao;

import com.zjy.esdemo.po.TestPo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TestDao extends ElasticsearchRepository<TestPo, Long> {
    List<TestPo> findByName(String name);

    List<TestPo> findByNameOrDesc(String text);
}
