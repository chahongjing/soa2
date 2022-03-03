package com.zjy.securityclient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/testPage")
    public String testPage(){
        return "testPage";
    }

    @GetMapping("/testAjax")
    @ResponseBody
    public String testAjax(){
        return "testAjax";
    }
}
