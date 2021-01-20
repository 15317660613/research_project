package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleConfigEO;
import com.adc.da.smallprogram.page.ScheduleConfigEOPage;
import com.adc.da.smallprogram.service.ScheduleConfigEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleConfig")
@Api(tags = "小程序配置|ScheduleConfigEO|")
public class ScheduleConfigEOController extends BaseController<ScheduleConfigEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleConfigEOController.class);

    @Autowired
    private ScheduleConfigEOService scheduleConfigEOService;

	@ApiOperation(value = "|ScheduleConfigEO|分页查询")
    @GetMapping("/page")
//    //@RequiresPermissions("smallprogram:scheduleConfig:page")
    public ResponseMessage<PageInfo<ScheduleConfigEO>> page(ScheduleConfigEOPage page) throws Exception {
        List<ScheduleConfigEO> rows = scheduleConfigEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleConfigEO|查询")
    @GetMapping("")
//    //@RequiresPermissions("smallprogram:scheduleConfig:list")
    public ResponseMessage<List<ScheduleConfigEO>> list(ScheduleConfigEOPage page) throws Exception {
	    return Result.success(scheduleConfigEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleConfigEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleConfig:get")
    public ResponseMessage<ScheduleConfigEO> find(@PathVariable String id) throws Exception {
	    return Result.success(scheduleConfigEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleConfigEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleConfig:save")
    public ResponseMessage<ScheduleConfigEO> create(@RequestBody ScheduleConfigEO scheduleConfigEO) throws Exception {
        scheduleConfigEOService.insertSelective(scheduleConfigEO);
        return Result.success(scheduleConfigEO);
    }

    @ApiOperation(value = "|ScheduleConfigEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleConfig:update")
    public ResponseMessage<ScheduleConfigEO> update(@RequestBody ScheduleConfigEO scheduleConfigEO) throws Exception {
        scheduleConfigEO.setUpdateTime(new Date());
	    scheduleConfigEOService.updateByPrimaryKeySelective(scheduleConfigEO);
        return Result.success(scheduleConfigEO);
    }

    @ApiOperation(value = "|ScheduleConfigEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleConfig:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleConfigEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_CONFIG where id = {}", id);
        return Result.success();
    }

}
