package com.zjy.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WorkOrderMapperExt {
    Map<String, Long> queryRepeatCount(@Param("id") Integer id, @Param("num") String num);
}