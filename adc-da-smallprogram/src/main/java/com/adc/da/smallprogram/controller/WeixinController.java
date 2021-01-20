
package com.adc.da.smallprogram.controller;

import com.adc.da.smallprogram.service.UserOpenidEOService;
import com.adc.da.smallprogram.service.WeiXinService;
import com.adc.da.smallprogram.vo.OpenIdReqVO;
import com.adc.da.smallprogram.vo.SendMessageReqVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/${restPath}/smallProgram/WeixinController")
@Api(description = "小程序微信API相关|WeixinController|")
public class WeixinController {

    @Autowired
    private WeiXinService weiXinService;
    @Autowired
    private UserOpenidEOService userOpenidEOService ;

    private static final String APP_SECRET = "7900b0ecf5abc56bd726eeb6fa7106e5";
    private static final String APP_ID = "wx2d22dbe581c9914c";


    /**
     * 获取微信openId
     * @param accessTokenReqVO
     */
    @PostMapping("/getOpenId")
    @ApiOperation(value = "获取openId")
    public ResponseMessage getOpenId(@RequestBody OpenIdReqVO accessTokenReqVO){
        accessTokenReqVO.setSecret(APP_SECRET);
        accessTokenReqVO.setAppId(APP_ID);
        return Result.success(weiXinService.getOpenId(accessTokenReqVO));
    }
    @PostMapping("/getAccessToken")
    @ApiOperation(value = "获取AccessToken")
    public ResponseMessage getAccessToken(@RequestBody OpenIdReqVO accessTokenReqVO){
        return weiXinService.getAccessToken(accessTokenReqVO);
    }
    @GetMapping("/checkToken")
    @ApiOperation(value = "校验微信消息推送Token")
    public void checkToken(HttpServletRequest request, HttpServletResponse response){
        weiXinService.checkToken(request,response);
    }

    @PostMapping("/sendMessage")
    @ApiOperation(value = "发送模板消息")
    public ResponseMessage sendMessage(@RequestBody SendMessageReqVO sendMessageReqVO){
        return weiXinService.sendMessage(sendMessageReqVO);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "解绑")
    public ResponseMessage<String> deleteByUserId(String userId){
        userOpenidEOService.deleteByUserId(userId);
        return Result.success("解绑成功！");
    }

    @ApiOperation("|UserEO|删除")
    @DeleteMapping({"/logoutArr/{ids}"})
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        userOpenidEOService.deleteByUserIdList(idList);
        return Result.success();
    }

}

