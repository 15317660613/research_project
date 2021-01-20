package com.adc.da.processform.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.processform.entity.ProjectBudgetEO;
import com.adc.da.processform.page.ProjectBudgetEOPage;
import com.adc.da.processform.service.ProjectBudgetEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/processform/projectBudget")
@Api(description = "|ProjectBudgetEO|")
public class ProjectBudgetEOController extends BaseController<ProjectBudgetEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectBudgetEOController.class);

    @Autowired
    private ProjectBudgetEOService projectBudgetEOService;

	@ApiOperation(value = "|ProjectBudgetEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("processform:projectBudget:page")
    public ResponseMessage<PageInfo<ProjectBudgetEO>> page(ProjectBudgetEOPage page) throws Exception {
        List<ProjectBudgetEO> rows = projectBudgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectBudgetEO|查询")
    @GetMapping("")
//    @RequiresPermissions("processform:projectBudget:list")
    public ResponseMessage<List<ProjectBudgetEO>> list(ProjectBudgetEOPage page) throws Exception {
        return Result.success(projectBudgetEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectBudgetEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("processform:projectBudget:get")
    public ResponseMessage<ProjectBudgetEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectBudgetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:projectBudget:save")
    public ResponseMessage<ProjectBudgetEO> create(@RequestBody ProjectBudgetEO projectBudgetEO) throws Exception {
        projectBudgetEOService.insertSelective(projectBudgetEO);
        return Result.success(projectBudgetEO);
    }

    @ApiOperation(value = "|ProjectBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:projectBudget:update")
    public ResponseMessage<ProjectBudgetEO> update(@RequestBody ProjectBudgetEO projectBudgetEO) throws Exception {
        projectBudgetEOService.updateByPrimaryKeySelective(projectBudgetEO);
        return Result.success(projectBudgetEO);
    }

    @ApiOperation(value = "|ProjectBudgetEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("processform:projectBudget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectBudgetEOService.deleteByPrimaryKey(id);
        logger.info("delete from PF_PROJECT_BUDGET where id = {}", id);
        return Result.success();
    }

}
