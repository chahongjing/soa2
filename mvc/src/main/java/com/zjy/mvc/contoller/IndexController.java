package com.zjy.mvc.contoller;

import com.alibaba.fastjson.JSON;
import com.zjy.mvc.common.stratory.BaseActionParam;
import com.zjy.mvc.common.stratory.BaseActionResult;
import com.zjy.mvc.common.stratory.EventDispatcher;
import com.zjy.mvc.common.stratory.close.CloseParam;
import com.zjy.mvc.common.stratory.create.CreateParam;
import com.zjy.mvc.po.DownloadTask;
import com.zjy.mvc.po.TestDownloadRecord;
import com.zjy.mvc.po.UserInfo;
import com.zjy.mvc.service.DownlaodTaskService;
import com.zjy.mvc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private DownlaodTaskService downlaodTaskService;

    @Autowired
    private EventDispatcher eventDispatcher;

    @GetMapping("/index")
    @ResponseBody
    public String index() {
        log.info("/comm/getEnums");
        return "abc";
    }
    @GetMapping("/comm/getEnums")
    @ResponseBody
    public String getEnums() {
        int a = 1, b = 0;
        return "abc";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");
        SavedRequest lastRequest = WebUtils.getSavedRequest(request);
        String lastUrl = null;
        if (lastRequest != null && HttpMethod.GET.toString().equalsIgnoreCase(lastRequest.getMethod())) {
            lastUrl = lastRequest.getRequestUrl();
        }
        if (StringUtils.isBlank(lastUrl)) {
            lastUrl = request.getRequestURL().toString().replace(request.getRequestURI(), StringUtils.EMPTY) + request.getContextPath() + "/";
        }
        mv.addObject("redirectUrl", lastUrl);
        return mv;
    }

    @GetMapping("/testPage")
    @RequiresPermissions({"myPer"})
    public String testPage() {
        UserInfo fromMaster = userService.getFromMaster();
        UserInfo fromSlave = userService.getFromSlave();

        return "testPage";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zjy", "pass");
        subject.login(token);
        return "abc";
    }

    @GetMapping("/testDownload")
    @ResponseBody
    public String testDownload() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zjy", "pass");
        subject.login(token);
        return "abc";
    }

    @GetMapping("/testDownloadList")
    @ResponseBody
    public String testDownloadList() {
        DownloadTask task = new DownloadTask();
        task.setCreatedBy("1");
        task.setCreatedName("ad");
        downlaodTaskService.addTask(task);
        return "abc";
    }

    @GetMapping("/testCreateDownloadData")
    @ResponseBody
    public String testCreateDownloadData() {
        TestDownloadRecord record;
        for(int i = 0; i < 100000; i++) {
            log.info("插入第{}条数据", i);
            record = new TestDownloadRecord();
            record.setUserId(String.valueOf(i));
            record.setUserName("admin" + i);
            record.setUserCode("" + i);
            downlaodTaskService.addRecord(record);
        }
        return "abc";
    }

    @GetMapping("/testDisp")
    @ResponseBody
    public String testDisp() {
        BaseActionParam baseActionParam = new CreateParam();
        ((CreateParam)baseActionParam).setType("1");
        BaseActionResult baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        baseActionParam = new CloseParam();
        baseActionResult = eventDispatcher.fireAction(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));


        baseActionResult = eventDispatcher.publishEvent(baseActionParam);
        log.info("result:{}", JSON.toJSONString(baseActionResult));
        return "abc";
    }
}
