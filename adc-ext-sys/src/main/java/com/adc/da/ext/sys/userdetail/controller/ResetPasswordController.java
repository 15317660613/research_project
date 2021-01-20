package com.adc.da.ext.sys.userdetail.controller;

import com.adc.da.ext.sys.userdetail.service.ResetPasswordService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/${restPath}/ResetPassword")
@Api("|ResetPassword|")
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @ApiOperation("|UserEO|重置密码")
    @GetMapping({"/{ids}"})
    public ResponseMessage resetPassword(@NotNull @PathVariable("ids") String[] ids, HttpServletRequest request) {
        String userId = (String)request.getSession().getAttribute("LOGIN_USER_ID");
        List<String> idList = Arrays.asList(ids);
        if (idList.contains(userId)) {
            return Result.error("r0018", "用户不能重置自己！");
        } else {
            this.resetPasswordService.resetPassword(idList);
            return Result.success();
        }
    }
}
