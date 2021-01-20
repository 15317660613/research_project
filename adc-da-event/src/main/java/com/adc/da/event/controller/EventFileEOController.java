package com.adc.da.event.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.event.entity.EventFileEO;
import com.adc.da.event.page.EventFileEOPage;
import com.adc.da.event.service.EventFileEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/${restPath}/event/eventFile")
@Api(tags = "工作简报|EventFileEO|")
public class EventFileEOController extends BaseController<EventFileEO>{

    private static final Logger logger = LoggerFactory.getLogger(EventFileEOController.class);

    @Autowired
    private EventFileEOService eventFileEOService;


	@ApiOperation(value = "|EventFileEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("event:eventFile:page")
    public ResponseMessage<PageInfo<EventFileEO>> page(EventFileEOPage page) throws Exception {
        List<EventFileEO> rows = eventFileEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|EventFileEO|根据eventId查询")
    @GetMapping("selectByEvent/{id}")
    //@RequiresPermissions("event:eventFile:page")
    public ResponseMessage<String> selectByEventId(@RequestParam("id") String eventId) throws Exception {
        return Result.success(eventFileEOService.selectByEventId(eventId));
    }

	@ApiOperation(value = "|EventFileEO|查询")
    @GetMapping("")
    //@RequiresPermissions("event:eventFile:list")
    public ResponseMessage<List<EventFileEO>> list(EventFileEOPage page) throws Exception {
        return Result.success(eventFileEOService.queryByList(page));
	}

    @ApiOperation(value = "|EventFileEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("event:eventFile:save")
    public ResponseMessage<EventFileEO> create(@RequestBody EventFileEO eventFileEO) throws Exception {
	    eventFileEOService.insertSelective(eventFileEO);
        return Result.success(eventFileEO);
    }





}
