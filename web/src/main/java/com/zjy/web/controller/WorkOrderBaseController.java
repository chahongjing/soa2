package com.zjy.web.controller;

import com.zjy.enums.WorkOrderType;
import com.zjy.service.WorkOrderBaseService;
import com.zjy.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 */
@RestController
@RequestMapping("workOrderBase")
public class WorkOrderBaseController extends BaseController {

    @Autowired
    protected WorkOrderBaseService workOrderService;

    protected WorkOrderType getWorkOrderType() {
        return workOrderService.getWorkOrderType();
    }

    @GetMapping("test")
    public String test(String abc, Date date) {
        logger.info("主要用来进行测试" + abc + " date:" + date);
        return workOrderService.myBaseMethod();
    }
}
