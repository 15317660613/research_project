package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ImplementationProcFileEO;
import com.adc.da.research.project.page.ImplementationProcFileEOPage;
import com.adc.da.research.project.service.ImplementationProcFileEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/${restPath}/research/project/implementationProcFile")
@Api(tags = "科研系统|科研项目执行过程文件|ImplementationProcFileEOController")
public class ImplementationProcFileEOController extends BaseController<ImplementationProcFileEO> {

    private static final Logger logger = LoggerFactory.getLogger(ImplementationProcFileEOController.class);

    @Autowired
    private ImplementationProcFileEOService implementationProcFileEOService;

	@ApiOperation(value = "|ImplementationProcFileEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:implementationProcFile:page")
    public ResponseMessage<PageInfo<ImplementationProcFileEO>> page(ImplementationProcFileEOPage page) throws Exception {
        List<ImplementationProcFileEO> rows = implementationProcFileEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ImplementationProcFileEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:implementationProcFile:list")
    public ResponseMessage<List<ImplementationProcFileEO>> list(ImplementationProcFileEOPage page) throws Exception {
        return Result.success(implementationProcFileEOService.queryByList(page));
	}

    @ApiOperation(value = "|ImplementationProcFileEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:implementationProcFile:get")
    public ResponseMessage<ImplementationProcFileEO> find(@PathVariable String id) throws Exception {
        return Result.success(implementationProcFileEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ImplementationProcFileEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:implementationProcFile:save")
    public ResponseMessage<ImplementationProcFileEO> create(@RequestBody ImplementationProcFileEO implementationProcFileEO) throws Exception {
        implementationProcFileEOService.insertSelective(implementationProcFileEO);
        return Result.success(implementationProcFileEO);
    }

    @ApiOperation(value = "|ImplementationProcFileEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:implementationProcFile:update")
    public ResponseMessage<ImplementationProcFileEO> update(@RequestBody ImplementationProcFileEO implementationProcFileEO) throws Exception {
        implementationProcFileEOService.updateByPrimaryKeySelective(implementationProcFileEO);
        return Result.success(implementationProcFileEO);
    }

    @ApiOperation(value = "|ImplementationProcFileEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:implementationProcFile:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        implementationProcFileEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_IMPLEMENTATION_PROC_FILE where id = {}", id);
        return Result.success();
    }

}
