package com.zjy.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjy.api.TestApi;
import com.zjy.service.MyUserService;
import com.zjy.web.service.KafkaProducer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/")
@Api(value = "IndexController|一个用来测试swagger注解的控制器")
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

    @Autowired
    private MyUserService myUserService;

    @GetMapping("index")@ApiOperation(value="这是一个测试的方法的概要", notes="这是这个方法的备注说明")
    @ApiImplicitParam(paramType="query", name = "id", value = "用户id", required = true, dataType = "Integer")
    public Map test(Integer id) {
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
