package com.zjy.esdemo.po;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(indexName = "student_index")
public class Student {
    @Id
    @Field(type = FieldType.Keyword)
    private Long studentId;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field("name_completion")
    @JSONField(name = "name_completion")
    private String nameCompletion;

    @Field(type = FieldType.Keyword)
    private String desc;

    @Field(type = FieldType.Integer)
    private Integer age;

    @Field(type = FieldType.Auto)
    private List<Double> scores;

    @Field(type = FieldType.Long)
    private Date birthday;

    @Field(type = FieldType.Object)
    private Address address;

    @Field(type = FieldType.Nested)
    private List<Interest> interests;

    public Student() {

    }

    public Student(Long studentId, String name, Integer age, List<Double> scores, Date birthday) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.scores = scores;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", scores=" + scores +
                '}';
    }
}
