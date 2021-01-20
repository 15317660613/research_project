package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ProjectTargetBottomEO;
import com.adc.da.crm.page.ProjectTargetBottomEOPage;
import com.adc.da.crm.service.ProjectTargetBottomEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/projectTargetBottom")
@Api(description = "|ProjectTargetBottomEO|")
public class ProjectTargetBottomEOController extends BaseController<ProjectTargetBottomEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectTargetBottomEOController.class);

    @Autowired
    private ProjectTargetBottomEOService projectTargetBottomEOService;

	@ApiOperation(value = "|ProjectTargetBottomEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:projectTargetBottom:page")
    public ResponseMessage<PageInfo<ProjectTargetBottomEO>> page(ProjectTargetBottomEOPage page) throws Exception {
        List<ProjectTargetBottomEO> rows = projectTargetBottomEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectTargetBottomEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:projectTargetBottom:list")
    public ResponseMessage<List<ProjectTargetBottomEO>> list(ProjectTargetBottomEOPage page) throws Exception {
        return Result.success(projectTargetBottomEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectTargetBottomEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:projectTargetBottom:get")
    public ResponseMessage<ProjectTargetBottomEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectTargetBottomEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectTargetBottomEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectTargetBottom:save")
    public ResponseMessage<ProjectTargetBottomEO> create(@RequestBody ProjectTargetBottomEO projectTargetBottomEO) throws Exception {
        projectTargetBottomEOService.insertSelective(projectTargetBottomEO);
        return Result.success(projectTargetBottomEO);
    }

    @ApiOperation(value = "|ProjectTargetBottomEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectTargetBottom:update")
    public ResponseMessage<ProjectTargetBottomEO> update(@RequestBody ProjectTargetBottomEO projectTargetBottomEO) throws Exception {
        projectTargetBottomEOService.updateByPrimaryKeySelective(projectTargetBottomEO);
        return Result.success(projectTargetBottomEO);
    }

    @ApiOperation(value = "|ProjectTargetBottomEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:projectTargetBottom:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectTargetBottomEOService.deleteByPrimaryKey(id);
        logger.info("delete from PROJECT_TARGET_BOTTOM where id = {}", id);
        return Result.success();
    }

}
