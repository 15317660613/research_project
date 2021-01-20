package com.adc.da.processform.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.processform.entity.OperateProjectExamEO;
import com.adc.da.processform.page.OperateProjectExamEOPage;
import com.adc.da.processform.service.OperateProjectExamEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/processform/operateProjectExam")
@Api(description = "|OperateProjectExamEO|")
public class OperateProjectExamEOController extends BaseController<OperateProjectExamEO>{

    private static final Logger logger = LoggerFactory.getLogger(OperateProjectExamEOController.class);

    @Autowired
    private OperateProjectExamEOService operateProjectExamEOService;

	@ApiOperation(value = "|OperateProjectExamEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("processform:operateProjectExam:page")
    public ResponseMessage<PageInfo<OperateProjectExamEO>> page(OperateProjectExamEOPage page) throws Exception {
        List<OperateProjectExamEO> rows = operateProjectExamEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|OperateProjectExamEO|查询")
    @GetMapping("")
//    @RequiresPermissions("processform:operateProjectExam:list")
    public ResponseMessage<List<OperateProjectExamEO>> list(OperateProjectExamEOPage page) throws Exception {
        return Result.success(operateProjectExamEOService.queryByList(page));
	}

    @ApiOperation(value = "|OperateProjectExamEO|详情")
    @GetMapping("/{projectid}")
//    @RequiresPermissions("processform:operateProjectExam:get")
    public ResponseMessage<OperateProjectExamEO> find(@PathVariable String projectid) throws Exception {
        return Result.success(operateProjectExamEOService.selectByPrimaryKey(projectid));
    }

    @ApiOperation(value = "|OperateProjectExamEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:operateProjectExam:save")
    public ResponseMessage<OperateProjectExamEO> create(@RequestBody OperateProjectExamEO operateProjectExamEO) throws Exception {
        operateProjectExamEOService.insertSelective(operateProjectExamEO);
        return Result.success(operateProjectExamEO);
    }

    @ApiOperation(value = "|OperateProjectExamEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:operateProjectExam:update")
    public ResponseMessage<OperateProjectExamEO> update(@RequestBody OperateProjectExamEO operateProjectExamEO) throws Exception {
        operateProjectExamEOService.updateByPrimaryKeySelective(operateProjectExamEO);
        return Result.success(operateProjectExamEO);
    }

    @ApiOperation(value = "|OperateProjectExamEO|删除")
    @DeleteMapping("/{projectid}")
//    @RequiresPermissions("processform:operateProjectExam:delete")
    public ResponseMessage delete(@PathVariable String projectid) throws Exception {
        operateProjectExamEOService.deleteByPrimaryKey(projectid);
        logger.info("delete from PF_OPERATE_PROJECT_EXAM where projectid = {}", projectid);
        return Result.success();
    }

}
