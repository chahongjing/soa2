package com.zjy.dao.common;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公共dao
 *
 * @author chahongjing
 * @create 2016-12-10 13:36
 */
public interface BaseDao<T, S> {
    long countByCondition(S example);

    int deleteByCondition(S example);

    int deleteById(Long id);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByCondition(S example);

    T selectById(Long id);

    int updateByConditionSelective(@Param("record") T record, @Param("example") S example);

    int updateByCondition(@Param("record") T record, @Param("example") S example);

    int updateByIdSelective(T record);

    int updateById(T record);
}
