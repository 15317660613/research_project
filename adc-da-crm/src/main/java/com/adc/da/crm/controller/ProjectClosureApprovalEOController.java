package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ProjectClosureApprovalEO;
import com.adc.da.crm.page.ProjectClosureApprovalEOPage;
import com.adc.da.crm.service.ProjectClosureApprovalEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/projectClosureApproval")
@Api(description = "|ProjectClosureApprovalEO|")
public class ProjectClosureApprovalEOController extends BaseController<ProjectClosureApprovalEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectClosureApprovalEOController.class);

    @Autowired
    private ProjectClosureApprovalEOService projectClosureApprovalEOService;

	@ApiOperation(value = "|ProjectClosureApprovalEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:projectClosureApproval:page")
    public ResponseMessage<PageInfo<ProjectClosureApprovalEO>> page(ProjectClosureApprovalEOPage page) throws Exception {
        List<ProjectClosureApprovalEO> rows = projectClosureApprovalEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectClosureApprovalEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:projectClosureApproval:list")
    public ResponseMessage<List<ProjectClosureApprovalEO>> list(ProjectClosureApprovalEOPage page) throws Exception {
        return Result.success(projectClosureApprovalEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectClosureApprovalEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectClosureApproval:save")
    public ResponseMessage<ProjectClosureApprovalEO> create(@RequestBody ProjectClosureApprovalEO projectClosureApprovalEO) throws Exception {
        projectClosureApprovalEOService.insertSelective(projectClosureApprovalEO);
        return Result.success(projectClosureApprovalEO);
    }

}
