package com.zjy.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjy.api.TestApi;
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
//    @Reference
    @Autowired
    private TestApi testApi;

    @GetMapping("index")
    public Map test() {
        Map<String, String> map = new HashMap<>();
        String test = testApi.test();
        map.put("name", test);
        return map;
    }
}
