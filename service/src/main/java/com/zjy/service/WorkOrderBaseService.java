package com.zjy.service;

import com.zjy.enums.WorkOrderType;
import com.zjy.po.WorkOrder;
import com.zjy.po.WorkOrderCondition;
import com.zjy.service.common.BaseService;

public interface WorkOrderBaseService extends BaseService<WorkOrder, WorkOrderCondition> {
    WorkOrderType getWorkOrderType();

    String myBaseMethod();
}
