package com.zjy.mvc.common.stratory;

import com.zjy.mvc.po.UserInfo;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

public class ActionEvent<P extends BaseActionParam, R extends BaseActionResult> extends ApplicationEvent {

    private P param;

    private R result;

    private Object oldIns;

    private Object newIns;

    private UserInfo opUser;

    private Date opDate;

    public ActionEvent(P param) {
        this(param, null);
    }

    public ActionEvent(P param, UserInfo userInfo) {
        this(param, userInfo, new Date());
    }

    public ActionEvent(P param, UserInfo userInfo, Date opDate) {
        super(param.getClass().getName());
        this.param = param;
        this.opUser = userInfo;
        this.opDate = opDate;
    }

    public P getParam() {
        return param;
    }

    public void setParam(P param) {
        this.param = param;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public Object getOldIns() {
        return oldIns;
    }

    public void setOldIns(Object oldIns) {
        this.oldIns = oldIns;
    }

    public Object getNewIns() {
        return newIns;
    }

    public void setNewIns(Object newIns) {
        this.newIns = newIns;
    }

    public UserInfo getOpUser() {
        return opUser;
    }

    public void setOpUser(UserInfo opUser) {
        this.opUser = opUser;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
}
