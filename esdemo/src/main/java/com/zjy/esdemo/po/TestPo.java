package com.zjy.esdemo.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "testdoc")
public class TestPo {

    public TestPo() {
    }

    public TestPo(long id, String name, String sex, String desc) {
        this.id = id;
        this.name = name;
//        this.age = age;
        this.sex = sex;
        this.desc = desc;
    }

    // 必须指定一个id，
    @Id
    private long id;
    // 这里配置了分词器，字段类型，可以不配置，默认也可
    @Field(type = FieldType.Text)
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String name;
//    private Integer age;
    @Field(type = FieldType.Keyword)
    private String sex;
    @Field(type = FieldType.Text)
    private String desc;
}
