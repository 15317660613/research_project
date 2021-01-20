package com.adc.da.smallprogram.controller;

import com.adc.da.smallprogram.service.SmallUserService;
import com.adc.da.smallprogram.vo.SmallLoginVO;
import com.adc.da.util.http.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${restPath}/smallProgram/SmallUserController")
@Api(description = "小程序openId相关|SmallUserController|")
public class SmallUserController {
    private static final Logger logger = LoggerFactory.getLogger(SmallUserController.class);
    @Autowired
    private SmallUserService smallUserService;

    @ApiOperation("根据openID获取用户")
    @GetMapping("/getUserByOpenId/{openId}")
    public ResponseMessage getUserByOpenId(@PathVariable String openId){
        return smallUserService.getUserByOpenId(openId);
    }

    @ApiOperation("注册用户")
    @GetMapping("/checkeUser")
    public ResponseMessage checkUser(SmallLoginVO smallLoginVO){
        return smallUserService.checkUser(smallLoginVO);
    }

}
