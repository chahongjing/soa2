package com.zjy.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjy.api.TestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 */
@RestController
@RequestMapping("/")
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);
    // 通过dubbo注入
    @Reference
//    @Autowired
    private TestApi dubboTestApi;

    // 通过starter方式注入
    @Autowired
    private TestApi starterTestApi;

    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("index")
    public Map test() {
        Map<String, String> map = new HashMap<>();
        String test = dubboTestApi.test();
        kafkaProducer.send("测试消息！");
        map.put("test", test);
        String name = starterTestApi.getName();
        System.out.println(name);
        map.put("name", name);
        logger.trace("log:trace");
        logger.debug("log:debug");
        logger.info("log:info");
        logger.warn("log:warn");
        logger.error("log:error");
        return map;
    }
}
