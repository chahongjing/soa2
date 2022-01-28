package com.zjy.service.common;

import com.github.pagehelper.PageHelper;
import com.zjy.dao.common.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公共service
 *
 * @author chahongjing
 * @create 2016-12-10 13:38
 */
@Transactional
public class BaseServiceImpl<Dao extends BaseDao<T, S>, T, S> implements BaseService<T, S> {
    /**
     * 公共dao
     */
    @Autowired
    private Dao dao;

    /**
     * 日志
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public long countByCondition(S example) {
        return dao.countByCondition(example);
    }

    public int deleteByCondition(S example) {
        return dao.deleteByCondition(example);
    }

    public int deleteById(Long id) {
        return dao.deleteById(id);
    }

    public int insert(T record) {
        return dao.insert(record);
    }

    public int insertSelective(T record) {
        return dao.insertSelective(record);
    }

    public List<T> selectByCondition(S example) {
        return dao.selectByCondition(example);
    }

    public T selectById(Long id) {
        return dao.selectById(id);
    }

    public int updateByConditionSelective(T record, S example) {
        return dao.updateByConditionSelective(record, example);
    }

    public int updateByCondition(T record, S example) {
        return dao.updateByCondition(record, example);
    }

    public int updateByIdSelective(T record) {
        return dao.updateById(record);
    }

    public int updateById(T record) {
        return dao.updateById(record);
    }

    /**
     * 查询简单列表（分页）
     *
     * @param example
     * @return
     */
    public PageBean<? extends T> queryPageList(PageInfomation page, S example) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize()).setOrderBy(page.getOrderBy());
        return new PageBean(this.selectByCondition(example));
    }

    public Dao getDao() {
        return dao;
    }
}
