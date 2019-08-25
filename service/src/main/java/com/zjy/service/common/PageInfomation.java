package com.zjy.service.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页信息
 *
 * @author chahongjing
 * @create 2016-12-27 21:07
 */
public class PageInfomation {
    public static final int DEFAULT_PAGENUM = 1;

    public static final int DEFAULT_PAGESIZE = 20;
    /**
     * 当前页码
     */
    private int pageNum;
    /**
     * 每页大小
     */
    private int pageSize;
    /**
     * 排序
     */
    private String orderBy;

    public PageInfomation() {
        this(PageInfomation.DEFAULT_PAGENUM, PageInfomation.DEFAULT_PAGESIZE);
    }

    public PageInfomation(int pageNum, int pageSize) {
        this(pageNum, pageSize, StringUtils.EMPTY);
    }

    public PageInfomation(int pageNum, int pageSize, String orderBy) {
        this.setPageNum(pageNum);
        this.setPageSize(pageSize);
        this.setOrderBy(orderBy);
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
