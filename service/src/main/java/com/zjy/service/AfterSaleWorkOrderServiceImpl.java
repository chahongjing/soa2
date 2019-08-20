package com.zjy.service;

import com.zjy.enums.WorkOrderType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 *
 */
@Service
public class AfterSaleWorkOrderServiceImpl extends WorkOrderBaseServiceImpl implements AfterSaleWorkOrderService {

    @PostConstruct
    public void AfterSaleWorkOrderController() {
        setWorkOrderType(WorkOrderType.AFTERSALE);
    }
}
