package com.zjy.dao;

import com.zjy.dao.common.BaseDao;
import com.zjy.po.WorkOrder;
import com.zjy.po.WorkOrderCondition;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface WorkOrderMapperExt {
    Map<String, Long> queryRepeatCount(@Param("id") Integer id, @Param("num") String num);
}