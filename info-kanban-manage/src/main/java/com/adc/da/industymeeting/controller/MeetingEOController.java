package com.adc.da.industymeeting.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.industymeeting.entity.MeetingEO;
import com.adc.da.industymeeting.page.MeetingEOPage;
import com.adc.da.industymeeting.service.MeetingEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/${restPath}/industymeeting/meeting")
@Api(description = "|MeetingEO|行业会议安排")
public class MeetingEOController extends BaseController<MeetingEO>{

    private static final Logger logger = LoggerFactory.getLogger(MeetingEOController.class);

    @Autowired
    private MeetingEOService meetingEOService;

	@ApiOperation(value = "|MeetingEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("industymeeting:meeting:page")
    public ResponseMessage<PageInfo<MeetingEO>> page(MeetingEOPage page) throws Exception {
        List<MeetingEO> rows = meetingEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|MeetingEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("industymeeting:meeting:list")
    public ResponseMessage<List<MeetingEO>> list(MeetingEOPage page) throws Exception {
        return Result.success(meetingEOService.queryByList(page));
	}

    @ApiOperation(value = "|MeetingEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("industymeeting:meeting:get")
    public ResponseMessage<MeetingEO> find(@PathVariable String id) throws Exception {
        return Result.success(meetingEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|MeetingEO|新增")
    @PostMapping(value = "/create",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("industymeeting:meeting:save")
    public ResponseMessage<MeetingEO> create(@RequestBody MeetingEO meetingEO) throws Exception {
        meetingEOService.save(meetingEO);
        return Result.success(meetingEO);
    }

    @ApiOperation(value = "|MeetingEO|修改")
    @PutMapping(value = "/update",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("industymeeting:meeting:update")
    public ResponseMessage<MeetingEO> update(@RequestBody MeetingEO meetingEO) throws Exception {
        meetingEOService.update(meetingEO);
        return Result.success(meetingEO);
    }

    @ApiOperation(value = "|MeetingEO|删除")
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("industymeeting:meeting:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        meetingEOService.logicDeleteByPrimaryKey(id);
        return Result.success();
    }


    @ApiOperation(value = "|MeetingEO|行业会议安排导入")
    @PostMapping(value = "/excelImportIndustyMeeting")
    public ResponseMessage excelImportIndustyMeeting(
            @RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams params = new ImportParams();
        List<ExcelVerifyHanlderErrorResult> errors = meetingEOService.excelImportVerify(params, inputStream);
        ResponseMessage result = Result.success();
        if (errors != null && !errors.isEmpty()) {
            result.setMessage(errors.toString());
        }
        return result;
    }

}
