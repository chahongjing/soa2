package com.zjy.service;

import com.zjy.dao.UserDao;
import com.zjy.enums.WorkOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@Primary
public class WorkOrderBaseServiceImpl implements WorkOrderBaseService {

    private WorkOrderType workOrderType = WorkOrderType.DEFAULT;

    @Autowired
    protected UserDao userDao;

    public WorkOrderType getWorkOrderType() {
        return workOrderType;
    }

    protected void setWorkOrderType(WorkOrderType type) {
        workOrderType = type;
    }

    @Override
    public String myBaseMethod() {
        System.out.println(getWorkOrderType().getName());
        return workOrderType.toString();
    }
}
