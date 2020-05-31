package com.jerry.jtakeaway.controller;


import com.jerry.jtakeaway.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api
@RestController
@RequestMapping("/authen")
public class loginController {

    @ApiOperation("用户认证操作")
    @PostMapping("/jwtLogin")
    public String jwtLogin(@RequestBody User user) {


        return "登录成功";
    }

}
