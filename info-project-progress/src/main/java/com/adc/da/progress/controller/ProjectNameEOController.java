package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.progress.service.ProjectNameEOService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

//@RestController
//@RequestMapping("/${restPath}/progress/projectName")
//@Api(description = "|ProjectNameEO|")
public class ProjectNameEOController extends BaseController<ProjectNameEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectNameEOController.class);

    @Autowired
    private ProjectNameEOService projectNameEOService;

    @ApiOperation(value = "|ProjectNameEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("progress:projectName:page")
    public ResponseMessage<PageInfo<ProjectNameEO>> page(ProjectNameEOPage page) throws Exception {
        List<ProjectNameEO> rows = projectNameEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectNameEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:projectName:list")
    public ResponseMessage<List<ProjectNameEO>> list(ProjectNameEOPage page) throws Exception {
        return Result.success(projectNameEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectNameEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:projectName:get")
    public ResponseMessage<ProjectNameEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectNameEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectNameEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectName:save")
    public ResponseMessage<ProjectNameEO> create(@RequestBody ProjectNameEO projectNameEO) throws Exception {
        projectNameEO.setId(UUID.randomUUID10());
        projectNameEOService.insertSelective(projectNameEO);
        return Result.success(projectNameEO);
    }

    @ApiOperation(value = "|ProjectNameEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectName:update")
    public ResponseMessage<ProjectNameEO> update(@RequestBody ProjectNameEO projectNameEO) throws Exception {
        projectNameEOService.updateByPrimaryKeySelective(projectNameEO);
        return Result.success(projectNameEO);
    }

    @ApiOperation(value = "|ProjectNameEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("progress:projectName:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectNameEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_PROJECT_NAME where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|StageOrderEO|逻辑删除，flag值为1")
    @DeleteMapping("/deleteByPrimaryKeysInFlag/{ids}")
    @RequiresPermissions("progress:stageOrder:delete")
    public ResponseMessage deleteByPrimaryKeysInFlag(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        projectNameEOService.deleteByPrimaryKeysInFlag(idList);
        logger.info("delete from PR_PROJECT_NAME where ids = {}", idList);
        return Result.success();
    }

    @ApiOperation(value = "Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("mybatis/mapper/file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        return Result.success(projectNameEOService.excelImport(is, params));
    }

}
