package com.zjy.mvc.contoller;

import com.zjy.mvc.configuration.shiro.ShiroTokenAdmin;
import com.zjy.mvc.po.UserInfo;
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
        UsernamePasswordToken token = new ShiroTokenAdmin("zjy", "pass");
        subject.login(token);
        return "abc";
    }
}
