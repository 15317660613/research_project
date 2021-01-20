package com.adc.da.attendance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.attendance.entity.ExportExcleDTO;
import com.adc.da.attendance.entity.ExportExcleRealDTO;
import com.adc.da.attendance.service.WorkDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.attendance.entity.WorkDateEO;
import com.adc.da.attendance.page.WorkDateEOPage;
import com.adc.da.attendance.service.WorkDateEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/attendance/workDate")
@Api(tags = "|WorkDateEO|")
public class WorkDateEOController extends BaseController<WorkDateEO>{

    private static final Logger logger = LoggerFactory.getLogger(WorkDateEOController.class);

    @Autowired
    private WorkDateEOService workDateEOService;

    @Autowired
    private WorkDateService workDateService;

    @ApiOperation(value = "|WorkDateEO|持久化日期")
    @PostMapping("/createCalendar")
//    @RequiresPermissions("attendance:workDate:list")
    public ResponseMessage createCalendar(@RequestBody ExportExcleRealDTO exportExcleDTO) throws Exception {
        Object obj = workDateService.HolidayForever(exportExcleDTO);
        return Result.success(obj);
    }

    @ApiOperation(value = "|WorkDateEO|根据起止时间获取工作日和休息日")
    @GetMapping("/getListByStartEnd")
   // @RequiresPermissions("attendance:workDate:list")
    public ResponseMessage getListByStartEnd(ExportExcleRealDTO exportExcleRealDTO) {
        return Result.success(workDateService.getListByStartEnd(exportExcleRealDTO));
    }




    @ApiOperation(value = "|WorkDateEO|分页查询")
    @GetMapping("/page")
 //   @RequiresPermissions("attendance:workDate:page")
    public ResponseMessage<PageInfo<WorkDateEO>> page(WorkDateEOPage page) throws Exception {
        List<WorkDateEO> rows = workDateEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|WorkDateEO|查询")
    @GetMapping("")
   // @RequiresPermissions("attendance:workDate:list")
    public ResponseMessage<List<WorkDateEO>> list(WorkDateEOPage page) throws Exception {
        return Result.success(workDateEOService.queryByList(page));
	}



    @ApiOperation(value = "|WorkDateEO|详情")
    @GetMapping("/{id}")
 //   @RequiresPermissions("attendance:workDate:get")
    public ResponseMessage<WorkDateEO> find(@PathVariable String id) throws Exception {
        return Result.success(workDateEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|WorkDateEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("attendance:workDate:save")
    public ResponseMessage<WorkDateEO> create(@RequestBody WorkDateEO workDateEO) throws Exception {
        workDateEOService.insertSelective(workDateEO);
        return Result.success(workDateEO);
    }

    @ApiOperation(value = "|WorkDateEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("attendance:workDate:update")
    public ResponseMessage<WorkDateEO> update(@RequestBody WorkDateEO workDateEO) throws Exception {
        workDateEOService.updateByPrimaryKeySelective(workDateEO);
        return Result.success(workDateEO);
    }

    @ApiOperation(value = "|WorkDateEO|删除")
    @DeleteMapping("/{id}")
 //   @RequiresPermissions("attendance:workDate:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        workDateEOService.deleteByPrimaryKey(id);
        logger.info("delete from WORK_DATE where id = {}", id);
        return Result.success();
    }

}
