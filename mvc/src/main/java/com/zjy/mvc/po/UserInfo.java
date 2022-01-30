package com.zjy.mvc.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserInfo {
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建日期
     */
    private Date createdOn;
    /**
     * 修改人
     */
    private String modifiedBy;
    /**
     * 修改日期
     */
    private Date modifiedOn;
    /**
     * 用户Guid
     */
//    @TableId
    private String userId;
    /**
     * 代码
     */
//    @JSONField(name="user_code")
    private String userCode;
    /**
     * 名称
     */
    private String userName;
}
