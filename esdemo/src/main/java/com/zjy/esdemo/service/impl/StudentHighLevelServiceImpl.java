package com.zjy.esdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjy.esdemo.po.Address;
import com.zjy.esdemo.po.Interest;
import com.zjy.esdemo.po.Student;
import com.zjy.esdemo.service.StudentHighLevelService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class StudentHighLevelServiceImpl implements StudentHighLevelService {

    @Resource
    private EsHighLevelOpt esHighLevelOpt;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void createIndex(String index) {
        try {
            esHighLevelOpt.createIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIndex(String index) {
        try {
            esHighLevelOpt.deleteIndex(index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(String index, Student student) {
        esHighLevelOpt.insertDoc(index, student.getStudentId().toString(), student);
    }

    @Override
    public void bulkInsert(String index, List<Student> studentList){
        esHighLevelOpt.bulkInsert(index, studentList);
    }

    @Override
    public void insertTestDoc(String index) {
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
        esHighLevelOpt.insertDoc(index, student.getStudentId().toString(), student);
    }

    @Override
    public void batchInsertTestDoc(String index) {
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
        esHighLevelOpt.bulkInsert(index, list);
//        esUtils.bulkIndex(index, list);
    }

    @Override
    public void update(String index, long id, int age) {
        esHighLevelOpt.updateDoc2(index, String.valueOf(id), age);
    }

    @Override
    public void deleteDoc(String index, String id) {
        esHighLevelOpt.deleteDoc(index, id);
    }

    public List<Student> search() {
        return esHighLevelOpt.mulitSearch();
    }

    @Override
    public List<Student> getList(String index, List<String> memberIds) {
        List<Student> memberList = new ArrayList<>();

        MultiSearchRequest request = new MultiSearchRequest();
        memberIds.forEach(memberId -> {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().
                    query(QueryBuilders.matchPhraseQuery("studentId", memberId));
            request.add(new SearchRequest()
                    .source(searchSourceBuilder)
                    .indices(index));
        });
        try {
            MultiSearchResponse response = restHighLevelClient.msearch(request, RequestOptions.DEFAULT);
            for (MultiSearchResponse.Item item : response.getResponses()) {
                for (SearchHit hit : item.getResponse().getHits().getHits()) {
                    memberList.add(JSON.parseObject(hit.getSourceAsString(), Student.class));
                }
            }
        } catch (IOException e) {
            log.error("es查询异常", e);
        }
        return memberList;
    }
}
