package com.adc.da.statistics.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.entity.AllBusinessStatistics;
import com.adc.da.budget.entity.FinOrgStatisticsEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.service.BusinessWorktimeEOService;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${restPath}/statistics/businessWorktime")
@Api(tags = "|定制化工时统计|")
public class BusinessWorktimeEOController extends BaseController<BusinessWorktimeEO> {

    @Autowired
    private BusinessWorktimeEOService businessWorktimeEOService;

    @Autowired
    private TimerInputBusinessWorkTime timerInputBusinessWorkTime;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    @ApiOperation(value = "|BusinessWorktimeEO|根据业务ID获取其12个月的工时数据")
    @GetMapping("/getManDayByMonth/{id}")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<List<BusinessWorktimeEO>> getManDayByMonth(@PathVariable String id) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String year = sdf.format(date);
        return Result.success(businessWorktimeEOService.getManDayByMonth(id, year));
    }

    @ApiOperation(value = "|BusinessWorktimeEO|根据自定义时间段进行工时查询")
    @GetMapping("/getOrgWorkTime")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<FinOrgStatisticsEO> getOrgWorkTime(String orgIds, String beginTime, String endTime,String requestType) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if ((subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.PROJECT_ADMIN)|| subject.hasRole(Role.ZHU_REN))&&StringUtils.equals(requestType,"bigDept")) {
            orgIds = "USW7ASDVED,MH8JQV5TSN";
        }

        return Result.success(businessWorktimeEOService.getOrgWorkTime(orgIds, beginTime, endTime,requestType));
    }

    @ApiOperation(value = "|BusinessWorktimeEO|启动工时转换存储服务")
    @GetMapping("/start")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<Boolean> getOrgWorkTime(boolean saveData) throws Exception {
        timerInputBusinessWorkTime.startTask(saveData);
        return Result.success(true);
    }

    @ApiOperation(value = "|BusinessWorktimeEO|根据自定义时间段进行工时查询")
    @GetMapping("/getBusWorkTime")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<AllBusinessStatistics> getProWorkTime(String orgIds, String beginTime, String endTime) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        return Result.success(businessWorktimeEOService.getBusWorkTime(orgIds, beginTime, endTime));
    }

    /**
     * 按部门统计工时
     * 就统计一下 本部 下各个部门花费工时的占比
     * 约定是本部的人访问这里
     */
    @ApiOperation("|WorkTime|工时统计组长及以上")
    @GetMapping("/getWorkTimeByHQ/{paramType}")
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByHQ(@PathVariable("paramType") String paramType) {
        return Result.success(businessWorktimeEOService.getWorkTimeByHQNew(paramType));
    }
}
