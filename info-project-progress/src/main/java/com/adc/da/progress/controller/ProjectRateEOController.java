package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.progress.entity.ProjectRateEO;
import com.adc.da.progress.page.ProjectRateEOPage;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
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

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

//@RestController
//@RequestMapping("/${restPath}/progress/projectRate")
//@Api(description = "|ProjectRateEO|")
public class ProjectRateEOController extends BaseController<ProjectRateEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectRateEOController.class);

    @Autowired
    private ProjectRateEOService projectRateEOService;

    @ApiOperation(value = "|ProjectRateEO|分页查询")
    @GetMapping("/com/adc/da/file/page")
//    @RequiresPermissions("progress:projectRate:page")
    public ResponseMessage<PageInfo<ProjectRateEO>> page(ProjectRateEOPage page) throws Exception {
        List<ProjectRateEO> rows = projectRateEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectRateEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:projectRate:list")
    public ResponseMessage<List<ProjectRateEO>> list(ProjectRateEOPage page) throws Exception {
        return Result.success(projectRateEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectRateEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:projectRate:get")
    public ResponseMessage<ProjectRateEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectRateEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectRateEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectRate:save")
    public ResponseMessage<ProjectRateEO> create(@RequestBody ProjectRateEO projectRateEO) throws Exception {
        projectRateEO.setId(UUID.randomUUID10());
        projectRateEOService.insertSelective(projectRateEO);
        return Result.success(projectRateEO);
    }

    @ApiOperation(value = "|ProjectRateEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectRate:update")
    public ResponseMessage<ProjectRateEO> update(@RequestBody ProjectRateEO projectRateEO) throws Exception {
        projectRateEOService.updateByPrimaryKeySelective(projectRateEO);
        return Result.success(projectRateEO);
    }

    @ApiOperation(value = "|ProjectRateEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("progress:projectRate:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectRateEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_PROJECT_RATE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|StageOrderEO|逻辑删除，flag值为1")
    @DeleteMapping("/deleteByPrimaryKeysInFlag/{ids}")
    @RequiresPermissions("progress:stageOrder:delete")
    public ResponseMessage deleteByPrimaryKeysInFlag(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        projectRateEOService.deleteByPrimaryKeysInFlag(idList);
        logger.info("delete from PR_PROJECT_RATE where ids = {}", idList);
        return Result.success();
    }

}
