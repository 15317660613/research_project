package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.SuspendAppEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.SuspendAppEOPage;
import com.adc.da.research.project.service.SuspendAppEOService;
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
@RequestMapping("/${restPath}/research/project/suspendApp")
@Api(tags = "科研系统|科研项目中止|SuspendAppEOController")
public class SuspendAppEOController extends BaseController<ProjectDataEO> {

    private static final Logger logger = LoggerFactory.getLogger(SuspendAppEOController.class);

    @Autowired
    private SuspendAppEOService suspendAppEOService;

	@ApiOperation(value = "|SuspendAppEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:suspendApp:page")
    public ResponseMessage<PageInfo<ProjectDataEO>> page(ProjectDataEOPage page) throws Exception {
        List<ProjectDataEO> rows = suspendAppEOService.getSuspendPage(page);
        return Result.success(getPageInfo(page.getPager(),rows));
    }

    @ApiOperation(value = "|SuspendAppEO|获取项目名称")
    @GetMapping("/getProjectName")
    public ResponseMessage<List<ProjectDataEO>> getProjectName(ProjectDataEOPage page) throws Exception{
	    return  Result.success(suspendAppEOService.getProjectName(page));
    }

	@ApiOperation(value = "|SuspendAppEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:suspendApp:list")
    public ResponseMessage<List<SuspendAppEO>> list(SuspendAppEOPage page) throws Exception {
        return Result.success(suspendAppEOService.queryByList(page));
	}

//	根据主键内容进行回显
    @ApiOperation(value = "|SuspendAppEO|详情")
    @GetMapping("/selectInfo")
//    @RequiresPermissions("research.project:suspendApp:get")
    public ResponseMessage<List<SuspendAppEO>> find( SuspendAppEOPage suspendAppEOPage) throws Exception {
        return Result.success(suspendAppEOService.selectInfoByPrimaryKey(suspendAppEOPage));
    }

    //    弹窗中的保存或修改功能  ----默认状态（待审核），插入数据
    @ApiOperation(value = "|SuspendAppEO|新增或修改")
    @PostMapping(value = "/insertOrUpdate",consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:suspendApp:save")
    public ResponseMessage<SuspendAppEO> create(@RequestBody SuspendAppEO suspendAppEO) throws Exception {
	    suspendAppEOService.insertOrUpdate(suspendAppEO);
        return Result.success(suspendAppEO);
    }


    @ApiOperation(value = "|SuspendAppEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:suspendApp:update")
    public ResponseMessage update(@RequestBody SuspendAppEO suspendAppEO) throws Exception {
        suspendAppEOService.updateInfoByPrimaryKey(suspendAppEO);
        return Result.success();
    }


    @ApiOperation(value = "|SuspendAppEO|删除")
    @DeleteMapping("/{projectId}")
//    @RequiresPermissions("research.project:suspendApp:delete")
    public ResponseMessage delete(@PathVariable String projectId) throws Exception {
        suspendAppEOService.deleteById(projectId);
        return Result.success();
    }

    //    弹窗中的提交功能  ----修改状态（审核中）
    @ApiOperation(value = "|SuspendAppEO|提交")
    @PostMapping("/submitInfo")
    public ResponseMessage submitInfo(@RequestBody SuspendAppEOPage suspendAppEO){
	    suspendAppEOService.submitInfo(suspendAppEO);
	    return Result.success();
    }


}
