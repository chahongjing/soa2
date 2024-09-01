package com.zjy.webapp;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
@Slf4j
public class IndexController {

    @GetMapping("index")
    public Map test(Integer id) {
        MDC.put("ttid", UUID.randomUUID().toString());

        MDC.put("ttttid", TraceContext.traceId());
        Map<String, String> map = new HashMap<>();
        log.trace("这是trace日志!");
        log.debug("这是debug日志!");
        log.info("这是info日志!");
        log.warn("这是warn日志!");
        try {
            int a = 1, b = 0;
            int c = a / b;
        }catch (Exception e) {
            log.error("这是error日志!", e);
        }
        map.put("msg", "访问成功!");
        return map;
    }

    @GetMapping("test")
    public Map test1(Integer id) {
        LinkedListIterationBenchMark.main(null);
        return null;
    }
}
