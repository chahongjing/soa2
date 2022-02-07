package com.zjy.mvc.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDownloadRecord {
    private String userId;
    /**
     * 代码
     */
    private String userCode;
    /**
     * 名称
     */
    private String userName;
}
