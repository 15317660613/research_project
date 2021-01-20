package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.ProcessStageEO;
import com.adc.da.research.config.page.ProcessStageEOPage;
import com.adc.da.research.config.service.ProcessStageEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/stage/processStage")
@Api(tags = "科研系统|科研项目流程阶段|ProcessStageEOController")
public class ProcessStageEOController extends BaseController<ProcessStageEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProcessStageEOController.class);

    @Autowired
    private ProcessStageEOService processStageEOService;

    @ApiOperation(value = "|ProcessStageEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("stage:processStage:page")
    public ResponseMessage<PageInfo<ProcessStageEO>> page(ProcessStageEOPage page) throws Exception {
        List<ProcessStageEO> rows = processStageEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProcessStageEO|查询")
    @GetMapping("")
    @RequiresPermissions("stage:processStage:list")
    public ResponseMessage<List<ProcessStageEO>> list(ProcessStageEOPage page) throws Exception {
        return Result.success(processStageEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProcessStageEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("stage:processStage:get")
    public ResponseMessage<ProcessStageEO> find(@PathVariable String id) throws Exception {
        return Result.success(processStageEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProcessStageEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("stage:processStage:save")
    public ResponseMessage<ProcessStageEO> create(@RequestBody ProcessStageEO processStageEO) throws Exception {
        processStageEOService.insertSelective(processStageEO);
        return Result.success(processStageEO);
    }

    @ApiOperation(value = "|ProcessStageEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("stage:processStage:update")
    public ResponseMessage<ProcessStageEO> update(@RequestBody ProcessStageEO processStageEO) throws Exception {
        processStageEOService.updateByPrimaryKeySelective(processStageEO);
        return Result.success(processStageEO);
    }

    @ApiOperation(value = "|ProcessStageEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("stage:processStage:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        processStageEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROCESS_STAGE where id = {}", id);
        return Result.success();
    }

}
