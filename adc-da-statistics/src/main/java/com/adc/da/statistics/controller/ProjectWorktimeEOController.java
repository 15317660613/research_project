package com.adc.da.statistics.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.BusinessStatistics;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import com.adc.da.statistics.service.ProjectWorktimeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/statistics/projectWorktime")
@Api(tags = "|定制化工时统计|")
public class ProjectWorktimeEOController extends BaseController<ProjectWorktimeEO> {

    @Autowired
    private ProjectWorktimeEOService projectWorktimeEOService;

    @ApiOperation(value = "|BusinessWorktimeEO|根据自定义时间段进行工时查询")
    @GetMapping("/getPBTaskWorkTimeByBusinessId")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<BusinessStatistics> getProWorkTime(String businessId, String beginTime, String endTime) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        return Result.success(projectWorktimeEOService.getPBWorkTime(businessId, beginTime, endTime, userId));
    }

    @ApiOperation(value = "|ProjectWorktimeEO|根据业务ID获取其12个月的工时数据")
    @GetMapping("/getManDayByMonth/{id}/{year}")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<List<ProjectWorktimeEO>> getManDayByMonth(@PathVariable(value = "id") String id,
        @PathVariable(value = "year") String year) {
        return Result.success(projectWorktimeEOService.getManDayByMonth(id, year));
    }

}
