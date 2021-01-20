package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ProjectWarnEO;
import com.adc.da.research.project.page.ProjectWarnEOPage;
import com.adc.da.research.project.service.ProjectWarnEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/projectWarn")
@Api(description = "项目预警发送信息列表|ProjectWarnEOController|")
public class ProjectWarnEOController extends BaseController<ProjectWarnEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectWarnEOController.class);

    @Autowired
    private ProjectWarnEOService projectWarnEOService;

	@ApiOperation(value = "|ProjectWarnEO|分页查询")
    @GetMapping("/page")
    // @RequiresPermissions("project:projectWarn:page")
    public ResponseMessage<PageInfo<ProjectWarnEO>> page(ProjectWarnEOPage page) throws Exception {
        List<ProjectWarnEO> rows = projectWarnEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectWarnEO|查询")
    @GetMapping("")
    // @RequiresPermissions("project:projectWarn:list")
    public ResponseMessage<List<ProjectWarnEO>> list(ProjectWarnEOPage page) throws Exception {
        return Result.success(projectWarnEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectWarnEO|详情")
    @GetMapping("/{id}")
    // @RequiresPermissions("project:projectWarn:get")
    public ResponseMessage<ProjectWarnEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectWarnEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectWarnEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    // @RequiresPermissions("project:projectWarn:save")
    public ResponseMessage<ProjectWarnEO> create(@RequestBody ProjectWarnEO projectWarnEO) throws Exception {
        projectWarnEOService.insertSelective(projectWarnEO);
        return Result.success(projectWarnEO);
    }

    @ApiOperation(value = "|ProjectWarnEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("project:projectWarn:update")
    public ResponseMessage<ProjectWarnEO> update(@RequestBody ProjectWarnEO projectWarnEO) throws Exception {
        projectWarnEOService.updateByPrimaryKeySelective(projectWarnEO);
        return Result.success(projectWarnEO);
    }

    @ApiOperation(value = "|ProjectWarnEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("project:projectWarn:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectWarnEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROJECT_WARN where id = {}", id);
        return Result.success();
    }

}
