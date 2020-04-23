package org.cp.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.cp.dubboapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by CP on 2020/4/20 0020.
 */
@RestController
public class UserController {

    @Reference(cluster = "failover", loadbalance = "random", timeout = 300, check = false)
    UserService service;

    @GetMapping("getuserbyid")
    public String getUserById(@RequestParam("id") Long id) {
        return service.getUserById(id).getName();
    }

}
