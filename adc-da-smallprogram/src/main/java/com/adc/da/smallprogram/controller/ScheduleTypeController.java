package com.adc.da.smallprogram.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.entity.ScheduleTypeEO;
import com.adc.da.smallprogram.page.ScheduleTypeEOPage;
import com.adc.da.smallprogram.service.ScheduleTypeService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/smallProgram/ScheduleTypeController")
@Api(description = "小程序日程类别|ScheduleTypeController|")
public class ScheduleTypeController extends BaseController<ScheduleTypeEO> {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourController.class);

    @Autowired
    private ScheduleTypeService scheduleTypeService;

    @ApiOperation("新增类型")
    @PostMapping("/save")
    public ResponseMessage save(@RequestBody ScheduleTypeEO scheduleHourEO) throws Exception{
        return scheduleTypeService.save(scheduleHourEO);
    }
    @ApiOperation("通过id获取类型")
    @GetMapping("/getById/{scheduleHourEOId}")
    public ResponseMessage getById(@PathVariable String scheduleHourEOId) throws Exception{
        return scheduleTypeService.getById(scheduleHourEOId);
    }

    @ApiOperation("通过id删除类型")
    @GetMapping("/deleteById/{scheduleHourEOId}")
    public ResponseMessage deleteById(@PathVariable String scheduleHourEOId) throws Exception{
        return scheduleTypeService.deleteById(scheduleHourEOId);
    }

    @ApiOperation("分页查询类型")
    @PostMapping("/page")
    public ResponseMessage page(@RequestBody ScheduleTypeEOPage scheduleHourEOPage) throws Exception{
        List<ScheduleTypeEO> rows = scheduleTypeService.page(scheduleHourEOPage);
        return Result.success(getPageInfo(scheduleHourEOPage.getPager(), rows));
    }
    @ApiOperation("不分页查询类型")
    @PostMapping("/list")
    public ResponseMessage list(@RequestBody ScheduleTypeEOPage scheduleHourEOPage) throws Exception{
        return scheduleTypeService.list(scheduleHourEOPage);
    }
}
