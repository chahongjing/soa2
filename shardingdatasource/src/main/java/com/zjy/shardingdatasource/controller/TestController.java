package com.zjy.shardingdatasource.controller;

import com.zjy.shardingdatasource.model.UserInfo;
import com.zjy.shardingdatasource.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {
    @Resource
    UserService userService;
    @GetMapping("/getById")
    public UserInfo getById(Long id) {
        return userService.getById(id);
    }
    @GetMapping("/getByIdList")
    public UserInfo getByIdList(String idListStr) {
        String[] split = idListStr.split("[,;]");
        List<Long> idList = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        return userService.getByIdList(idList);
    }
    @GetMapping("/getLog")
    public UserInfo getLog(Long id) {
        return userService.getLog(id);
    }
}
