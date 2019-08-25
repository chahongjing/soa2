package com.zjy.service.common;

/**
 * Created by Administrator on 2018/12/11.
 */

import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

/**
 * <b>类   名：</b>PageBean<br/>
 * <b>类描述：</b>描述这个类的功能<br/>
 * <b>创建人：</b>longzhao<br/>
 * <b>创建时间：</b>2016-2-29 上午10:18:38<br/>
 * <b>修改人：</b>longzhao<br/>
 * <b>修改时间：</b>2016-2-29 上午10:18:38<br/>
 * <b>修改备注：</b><br/>
 */
public class PageBean<T> implements Serializable {
    private static final long serialVersionUID = 8656597559014685635L;
    // 结果集
    private List<T> list;
    // 第几页
    private int pageNum;
    // 每页记录数
    private int pageSize;
    // 总页数
    private int pages;
    // 总记录数
    private long total;

    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     *
     * @param list page结果
     */
    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.list = page;
        }
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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

    public int getPages() {
        return pages == 0 ? 1 : pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
