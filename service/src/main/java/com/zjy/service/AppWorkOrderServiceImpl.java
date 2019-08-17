package com.zjy.service;

import com.zjy.enums.WorkOrderType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * TODO
 */
@Service
public class AppWorkOrderServiceImpl extends WorkOrderBaseServiceImpl implements AppWorkOrderService {

    @PostConstruct
    public void AfterSaleWorkOrderController() {
        setWorkOrderType(WorkOrderType.APP);
    }
}
