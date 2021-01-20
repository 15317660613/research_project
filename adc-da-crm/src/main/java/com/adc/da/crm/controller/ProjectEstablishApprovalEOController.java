package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ProjectEstablishApprovalEO;
import com.adc.da.crm.page.ProjectEstablishApprovalEOPage;
import com.adc.da.crm.service.ProjectEstablishApprovalEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/projectEstablishApproval")
@Api(description = "|ProjectEstablishApprovalEO|")
public class ProjectEstablishApprovalEOController extends BaseController<ProjectEstablishApprovalEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectEstablishApprovalEOController.class);

    @Autowired
    private ProjectEstablishApprovalEOService projectEstablishApprovalEOService;

	@ApiOperation(value = "|ProjectEstablishApprovalEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:projectEstablishApproval:page")
    public ResponseMessage<PageInfo<ProjectEstablishApprovalEO>> page(ProjectEstablishApprovalEOPage page) throws Exception {
        List<ProjectEstablishApprovalEO> rows = projectEstablishApprovalEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectEstablishApprovalEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:projectEstablishApproval:list")
    public ResponseMessage<List<ProjectEstablishApprovalEO>> list(ProjectEstablishApprovalEOPage page) throws Exception {
        return Result.success(projectEstablishApprovalEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectEstablishApprovalEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:projectEstablishApproval:get")
    public ResponseMessage<ProjectEstablishApprovalEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectEstablishApprovalEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectEstablishApprovalEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectEstablishApproval:save")
    public ResponseMessage<ProjectEstablishApprovalEO> create(@RequestBody ProjectEstablishApprovalEO projectEstablishApprovalEO) throws Exception {
        projectEstablishApprovalEOService.insertSelective(projectEstablishApprovalEO);
        return Result.success(projectEstablishApprovalEO);
    }

    @ApiOperation(value = "|ProjectEstablishApprovalEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectEstablishApproval:update")
    public ResponseMessage<ProjectEstablishApprovalEO> update(@RequestBody ProjectEstablishApprovalEO projectEstablishApprovalEO) throws Exception {
        projectEstablishApprovalEOService.updateByPrimaryKeySelective(projectEstablishApprovalEO);
        return Result.success(projectEstablishApprovalEO);
    }

    @ApiOperation(value = "|ProjectEstablishApprovalEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:projectEstablishApproval:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectEstablishApprovalEOService.deleteByPrimaryKey(id);
        logger.info("delete from PROJECT_ESTABLISH_APPROVAL where id = {}", id);
        return Result.success();
    }

}
