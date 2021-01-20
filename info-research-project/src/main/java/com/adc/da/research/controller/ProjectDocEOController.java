package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.ProjectDocEO;
import com.adc.da.research.page.ProjectDocEOPage;
import com.adc.da.research.service.ProjectDocEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/${restPath}/research/projectDoc")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class ProjectDocEOController extends BaseController<ProjectDocEO> {

    @Autowired
    private ProjectDocEOService projectDocEOService;

    @ApiOperation(value = "|ProjectDocEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("research:projectDoc:page")
    public ResponseMessage<PageInfo<ProjectDocEO>> page(ProjectDocEOPage page) throws Exception {
        List<ProjectDocEO> rows = projectDocEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectDocEO|查询")
    @GetMapping("")
    //@RequiresPermissions("research:projectDoc:list")
    public ResponseMessage<List<ProjectDocEO>> list(ProjectDocEOPage page) throws Exception {
        return Result.success(projectDocEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectDocEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("research:projectDoc:get")
    public ResponseMessage<ProjectDocEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectDocEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectDocEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("research:projectDoc:save")
    public ResponseMessage<ProjectDocEO> create(@RequestBody ProjectDocEO projectDocEO) throws Exception {
        projectDocEO.setId(UUID.randomUUID10());
        projectDocEOService.insertSelective(projectDocEO);
        return Result.success(projectDocEO);
    }

    @ApiOperation(value = "|ProjectDocEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("research:projectDoc:update")
    public ResponseMessage<ProjectDocEO> update(@RequestBody ProjectDocEO projectDocEO) throws Exception {
        projectDocEOService.updateByPrimaryKeySelective(projectDocEO);
        return Result.success(projectDocEO);
    }

    @ApiOperation(value = "|ProjectDocEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("research:projectDoc:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectDocEOService.deleteByPrimaryKey(id);
        log.info("delete from RS_PROJECT_DOC where id = {}", id);
        return Result.success();
    }

}
