package com.zjy.mongodb;

import com.zjy.mongodb.dynamicDatasource.DatasourceType;
import com.zjy.mongodb.dynamicDatasource.DynamicType;
import com.zjy.mongodb.master.MasterRep;
import com.zjy.mongodb.slave.SlaveRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {
    @Autowired
    private MasterRep masterRep;
    @Autowired
    private SlaveRep slaveRep;

    @DynamicType(type = DatasourceType.SLAVE)
    @ResponseBody
    @GetMapping("/index")
    public List<User> index() {
        return masterRep.findAll();
    }

    @DynamicType
    @ResponseBody
    @GetMapping("/index1")
    public List<User> index1() {
        return slaveRep.findAll();
    }

    @GetMapping("/insert")
    public String insert() {
        User user = new User("zjy", "123");
        masterRep.insert(user);
        user = new User("xxc", "456");
        slaveRep.insert(user);
        return "ok";
    }
}
