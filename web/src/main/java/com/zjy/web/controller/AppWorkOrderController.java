package com.zjy.web.controller;

import com.zjy.service.AppWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 *
 */
@RestController
@RequestMapping("appWorkOrder")
public class AppWorkOrderController extends WorkOrderBaseController {

    @Autowired
    private AppWorkOrderService appWorkOrderService;

    @PostConstruct
    public void AppWorkOrderController() {
        this.workOrderService = appWorkOrderService;
    }
}
