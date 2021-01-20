package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleResearchMarkEO;
import com.adc.da.smallprogram.page.ScheduleResearchMarkEOPage;
import com.adc.da.smallprogram.service.ScheduleResearchMarkEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleResearchMark")
@Api(tags = "小程序科委会用户标记|ScheduleResearchMarkEO|")
public class ScheduleResearchMarkEOController extends BaseController<ScheduleResearchMarkEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchMarkEOController.class);

    @Autowired
    private ScheduleResearchMarkEOService scheduleResearchMarkEOService;

	@ApiOperation(value = "|ScheduleResearchMarkEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("smallprogram:scheduleResearchMark:page")
    public ResponseMessage<PageInfo<ScheduleResearchMarkEO>> page(ScheduleResearchMarkEOPage page) throws Exception {
        List<ScheduleResearchMarkEO> rows = scheduleResearchMarkEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleResearchMarkEO|查询")
    @GetMapping("")
    //@RequiresPermissions("smallprogram:scheduleResearchMark:list")
    public ResponseMessage<List<ScheduleResearchMarkEO>> list(ScheduleResearchMarkEOPage page) throws Exception {
        return Result.success(scheduleResearchMarkEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleResearchMarkEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearchMark:get")
    public ResponseMessage<ScheduleResearchMarkEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleResearchMarkEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleResearchMarkEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearchMark:save")
    public ResponseMessage<ScheduleResearchMarkEO> create(@RequestBody ScheduleResearchMarkEO scheduleResearchMarkEO) throws Exception {
        scheduleResearchMarkEOService.insertSelective(scheduleResearchMarkEO);
        return Result.success(scheduleResearchMarkEO);
    }

    @ApiOperation(value = "|ScheduleResearchMarkEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearchMark:update")
    public ResponseMessage<ScheduleResearchMarkEO> update(@RequestBody ScheduleResearchMarkEO scheduleResearchMarkEO) throws Exception {
        scheduleResearchMarkEOService.updateByPrimaryKeySelective(scheduleResearchMarkEO);
        return Result.success(scheduleResearchMarkEO);
    }

    @ApiOperation(value = "|ScheduleResearchMarkEO|置顶或者更新")
    @PutMapping(value = "/updateTopOrCollect",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearchMark:update")
    public ResponseMessage<ScheduleResearchMarkEO> updateTopOrCollect(@RequestBody ScheduleResearchMarkEO scheduleResearchMarkEO) throws Exception {
        scheduleResearchMarkEOService.updateTopOrCollect(scheduleResearchMarkEO);
        return Result.success(scheduleResearchMarkEO);
    }


    @ApiOperation(value = "|ScheduleResearchMarkEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearchMark:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleResearchMarkEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_RESEARCH_MARK where id = {}", id);
        return Result.success();
    }

}
