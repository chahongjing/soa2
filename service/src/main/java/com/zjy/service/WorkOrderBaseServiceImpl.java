package com.zjy.service;

import com.zjy.dao.WorkOrderMapper;
import com.zjy.dao.WorkOrderMapperExt;
import com.zjy.enums.WorkOrderType;
import com.zjy.po.WorkOrder;
import com.zjy.po.WorkOrderCondition;
import com.zjy.service.common.BaseServiceImpl;
import com.zjy.service.common.PageBean;
import com.zjy.service.common.PageInfomation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 */
@Service
@Primary
public class WorkOrderBaseServiceImpl extends BaseServiceImpl<WorkOrderMapper, WorkOrder, WorkOrderCondition> implements WorkOrderBaseService {

    private WorkOrderType workOrderType = WorkOrderType.DEFAULT;

    public WorkOrderType getWorkOrderType() {
        return workOrderType;
    }

    protected void setWorkOrderType(WorkOrderType type) {
        workOrderType = type;
    }

    @Override
    public String myBaseMethod() {
        WorkOrder workOrder = super.selectById(1L);
        Map<String, Long> stringIntegerMap = this.getDao().queryRepeatCount(1, "zjy");
        PageInfomation pi = new PageInfomation(1, 10, "id desc");
        WorkOrderCondition woc = new WorkOrderCondition();
        PageBean<? extends WorkOrder> pageBean = super.queryPageList(pi, woc);
        System.out.println(getWorkOrderType().getName());
        return workOrderType.toString();
    }
}
