package com.adc.da.leaderview.controller.operate;

import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.milepost.MilepostQuery;
import com.adc.da.leaderview.service.operate.MilepostLeaderViewService;
import com.adc.da.leaderview.service.operate.ProjectLeaderViewService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.page.MyProjectMilepostEOPage;
import com.adc.da.progress.service.ProjectMilepostEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${restPath}/operate/milepostLeaderView")
@Api(tags = "|领导视角|")
public class MilepostLeaderViewController {

    @Autowired
    private MilepostLeaderViewService milepostLeaderViewService ;

    @ApiOperation(value = "||查询")
    @PostMapping("/searchByLoginUser")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<PageInfo<ProjectMilepostEO>> searchByLoginUser(@RequestBody MilepostQuery page)throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return Result.success(milepostLeaderViewService.searchByLoginUser(userId,page));
    }


}
