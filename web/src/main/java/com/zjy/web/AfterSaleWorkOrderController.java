package com.zjy.web;

import com.zjy.service.AfterSaleWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * TODO
 */
@RestController
@RequestMapping("afterSaleWorkOrder")
public class AfterSaleWorkOrderController extends WorkOrderBaseController {

    @Autowired
    private AfterSaleWorkOrderService afterSaleWorkOrderService;

    @PostConstruct
    public void AfterSaleWorkOrderController() {
        this.workOrderService = afterSaleWorkOrderService;
    }
}
