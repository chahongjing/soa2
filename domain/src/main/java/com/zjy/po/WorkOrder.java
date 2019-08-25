package com.zjy.po;

import java.io.Serializable;

/**
 * 
 * @author jyzeng
 * @date 2019-08-25 14:41:55
 */
public class WorkOrder implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String num;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num == null ? null : num.trim();
    }
}