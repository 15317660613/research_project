package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleImportantEO;
import com.adc.da.smallprogram.page.ScheduleImportantEOPage;
import com.adc.da.smallprogram.service.ScheduleImportantEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleImportant")
@Api(description = "|ScheduleImportantEO|")
public class ScheduleImportantEOController extends BaseController<ScheduleImportantEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleImportantEOController.class);

    @Autowired
    private ScheduleImportantEOService scheduleImportantEOService;


	@ApiOperation(value = "|ScheduleImportantEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("smallprogram:scheduleImportant:page")
    public ResponseMessage<PageInfo<ScheduleImportantEO>> page(ScheduleImportantEOPage page) throws Exception {
        List<ScheduleImportantEO> rows = scheduleImportantEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleImportantEO|查询")
    @GetMapping("")
//    @RequiresPermissions("smallprogram:scheduleImportant:list")
    public ResponseMessage<List<ScheduleImportantEO>> list(ScheduleImportantEOPage page) throws Exception {
        return Result.success(scheduleImportantEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleImportantEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleImportant:get")
    public ResponseMessage<ScheduleImportantEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleImportantEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleImportantEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleImportant:save")
    public ResponseMessage<ScheduleImportantEO> create(@RequestBody ScheduleImportantEO scheduleImportantEO) throws Exception {
        if (StringUtils.isNotEmpty(scheduleImportantEO.getName())) {
            scheduleImportantEOService.checkSameName(scheduleImportantEO.getName());
        }else {
            throw new AdcDaBaseException("文件名不能为空");
        }
	    scheduleImportantEO.setId(UUID.randomUUID10());
	    scheduleImportantEO.setCreateTime(new Date());
        scheduleImportantEOService.insertSelective(scheduleImportantEO);
        return Result.success(scheduleImportantEO);
    }

    @ApiOperation(value = "|ScheduleImportantEO|新增")
    @PostMapping("/create")
//    @RequiresPermissions("smallprogram:scheduleImportant:save")
    public ResponseMessage<ScheduleImportantEO> createNew(@RequestParam("createUserId") String createUserId, @RequestParam("file") MultipartFile file) throws Exception {
	    ScheduleImportantEO scheduleImportantEO = new ScheduleImportantEO();
	    scheduleImportantEO.setCreateUserId(createUserId);
        scheduleImportantEOService.insertSelectiveNew(scheduleImportantEO,file);
        return Result.success(scheduleImportantEO);
    }

    @ApiOperation(value = "|ScheduleImportantEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleImportant:update")
    public ResponseMessage<ScheduleImportantEO> update(@RequestBody ScheduleImportantEO scheduleImportantEO) throws Exception {
        scheduleImportantEOService.updateByPrimaryKeySelective(scheduleImportantEO);
        return Result.success(scheduleImportantEO);
    }

    @ApiOperation(value = "|ScheduleImportantEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleImportant:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
//        scheduleImportantEOService.deleteByPrimaryKey(id);
        scheduleImportantEOService.softDeleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_IMPORTANT where id = {}", id);
        return Result.success();
    }

}
