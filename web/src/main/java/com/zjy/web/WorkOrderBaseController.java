package com.zjy.web;

import com.zjy.enums.WorkOrderType;
import com.zjy.service.WorkOrderBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String test(String abc) {
        logger.info("主要用来进行测试" + abc);
        return workOrderService.myBaseMethod();
    }
}
