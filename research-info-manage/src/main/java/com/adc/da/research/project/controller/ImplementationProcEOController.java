package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ImplementationProcEO;
import com.adc.da.research.project.page.ImplementationProcEOPage;
import com.adc.da.research.project.service.ImplementationProcEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/project/implementationProc")
@Api(tags = "科研系统|科研项目执行过程|ImplementationProcEOController")
public class ImplementationProcEOController extends BaseController<ImplementationProcEO> {

    private static final Logger logger = LoggerFactory.getLogger(ImplementationProcEOController.class);

    @Autowired
    private ImplementationProcEOService implementationProcEOService;

	@ApiOperation(value = "|ImplementationProcEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:implementationProc:page")
    public ResponseMessage<PageInfo<ImplementationProcEO>> page(ImplementationProcEOPage page) throws Exception {
        List<ImplementationProcEO> rows = implementationProcEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ImplementationProcEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:implementationProc:list")
    public ResponseMessage<List<ImplementationProcEO>> list(ImplementationProcEOPage page) throws Exception {
        return Result.success(implementationProcEOService.queryByList(page));
	}

    @ApiOperation(value = "|ImplementationProcEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:implementationProc:get")
    public ResponseMessage<ImplementationProcEO> find(@PathVariable String id) throws Exception {
        return Result.success(implementationProcEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ImplementationProcEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:implementationProc:save")
    public ResponseMessage<ImplementationProcEO> create(@RequestBody ImplementationProcEO implementationProcEO) throws Exception {
        implementationProcEOService.insertSelective(implementationProcEO);
        return Result.success(implementationProcEO);
    }

    @ApiOperation(value = "|ImplementationProcEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:implementationProc:update")
    public ResponseMessage<ImplementationProcEO> update(@RequestBody ImplementationProcEO implementationProcEO) throws Exception {
        implementationProcEOService.updateByPrimaryKeySelective(implementationProcEO);
        return Result.success(implementationProcEO);
    }

    @ApiOperation(value = "|ImplementationProcEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:implementationProc:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        implementationProcEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_IMPLEMENTATION_PROC where id = {}", id);
        return Result.success();
    }

}
