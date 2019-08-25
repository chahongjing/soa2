package com.zjy.service.common;

import java.util.List;

public interface BaseService<T, S> {

    int countByCondition(S example);

    int deleteByCondition(S example);

    int deleteById(Long id);

    int insert(T record);

    int insertSelective(T record);

    List<T> selectByCondition(S example);

    T selectById(Long id);

    int updateByConditionSelective(T record, S example);

    int updateByCondition(T record, S example);

    int updateByIdSelective(T record);

    int updateById(T record);

    PageBean<? extends T> queryPageList(PageInfomation page, S example);
}
