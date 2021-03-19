package com.zjy.esdemo.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "student_index", type = "student")
public class Student {
    @Id
    @Field(type= FieldType.Auto)
    private String studentId;

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", scores=" + scores +
                '}';
    }

    public Student(String studentId, String name, Integer age, List<Double> scores) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.scores = scores;
    }

    public Student() {

    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Double> getScores() {
        return scores;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
    }

    @Field(type=FieldType.Auto)
    private String name;

    @Field(type=FieldType.Auto)
    private Integer age;

    @Field(type=FieldType.Auto)
    private List<Double> scores;
}
