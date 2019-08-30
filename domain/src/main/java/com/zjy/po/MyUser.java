package com.zjy.po;

import java.io.Serializable;

/**
 * 
 * @author jyzeng
 * @date 2019-08-30 21:30:49
 */
public class MyUser implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String name;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}