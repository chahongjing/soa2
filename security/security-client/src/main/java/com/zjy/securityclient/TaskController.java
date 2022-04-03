package com.zjy.securityclient;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by echisan on 2018/6/23
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public String listTasks(){
        return "任务列表";
    }
    @GetMapping("/withAuthority")
    @PreAuthorize("hasAuthority('myAuthority')")
    public String withAuthority(){
        return "任务列表有权限的列表";
    }

    @GetMapping("/withRole")
    @PreAuthorize("hasRole('myRole')")
    public String withRole(){
        return "任务列表有角色的列表";
    }

    @PutMapping("/{taskId}")
    public String updateTasks(@PathVariable("taskId")Integer id){
        return "更新了一下id为:"+id+"的任务";
    }

    @PostMapping
    public String newTasks(){
        return "创建了一个新的任务";
    }

    @DeleteMapping("/{taskId}")
    public String deleteTasks(@PathVariable("taskId")Integer id){
        return "删除了id为:"+id+"的任务";
    }
}
