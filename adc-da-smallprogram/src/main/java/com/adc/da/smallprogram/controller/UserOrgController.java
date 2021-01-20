package com.adc.da.smallprogram.controller;

import com.adc.da.smallprogram.service.UserOrgService;
import com.adc.da.smallprogram.vo.SmallUserVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/${restPath}/smallProgram/UserOrgController")
@Api(description = "小程序用户相关||UserOrgController|")
public class UserOrgController {
    private static final Logger logger = LoggerFactory.getLogger(UserOrgController.class);

    @Autowired
    private UserOrgService userOrgService;

    @ApiOperation("查询用户")
    @GetMapping("/queryUser/{orgId}")
    public ResponseMessage queryUser(@PathVariable String orgId)  {
        return userOrgService.queryUserNew(orgId);
    }

    @ApiOperation("查询机构和机构对应的人数")
    @GetMapping("/queryUserNum/{orgId}")
    public ResponseMessage queryUserNum(@PathVariable String orgId)  {
        return userOrgService.queryUserNum(orgId);
    }

//    @ApiOperation("查询用户")
//    @GetMapping("/findUserByName/{userName}/{queryType}")
//    public ResponseMessage findUserByName(@PathVariable String userName,@PathVariable String queryType) throws Exception{
//        return userOrgService.findUserByName(userName,queryType);
//    }

    @ApiOperation("全模糊查询，符合关键词的用户名及符合关键词的组织机构名下的所有用户都将被查到")
    @GetMapping("/findUserByName/{keyword}/{orgId}")
    public ResponseMessage findUserByKeyword(@PathVariable String keyword,@PathVariable String orgId) throws Exception{
        if (orgId.equals("index")){
            orgId = null ;
        }
        return userOrgService.findUserByKeyword(keyword,orgId);
    }



    @ApiOperation("查询小组下所有用户")
    @GetMapping("/queryAllUser/{orgId}")
    public ResponseMessage queryAllUser(@PathVariable String orgId)  {
        return userOrgService.queryAllUser(orgId);
    }

    @ApiOperation(value = "更改用户信息")
    @PostMapping("/updateUserEO")
    public ResponseMessage<SmallUserVO> updateUserEOByPrimaryKeySelective(@RequestBody UserEO userEO){
      return Result.success(userOrgService.updateUserEOByPrimaryKeySelective(userEO));

    }


    @ApiOperation(value = "更改用户密码")
    @PostMapping("/updateUserEOPwd")
    public ResponseMessage<String> updateUserEOByPrimaryKeySelective(
            @RequestBody HashMap<String, String> map){
        String usid = map.get("usid");
        String oldPWD = map.get("oldPWD");
        String newPWD = map.get("newPWD");
        return userOrgService.updatePWD(usid,oldPWD,newPWD);

    }


}
