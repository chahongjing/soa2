package com.zjy.service;

import com.zjy.enums.WorkOrderType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Service
@Primary
public class WorkOrderBaseServiceImpl implements WorkOrderBaseService {


    private WorkOrderType workOrderType = WorkOrderType.DEFAULT;

    public WorkOrderType getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(WorkOrderType type) {
        workOrderType = type;
    }

    @Override
    public String myBaseMethod() {
        System.out.println(getWorkOrderType().getName());
        return workOrderType.toString();
    }
}
