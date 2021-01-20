package com.adc.da.research.company.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.company.entity.CompanyEO;
import com.adc.da.research.company.page.CompanyEOPage;
import com.adc.da.research.company.service.CompanyEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/company/company")
@Api(tags = "科研系统|CompanyEO|")
public class CompanyEOController extends BaseController<CompanyEO>{

    private static final Logger logger = LoggerFactory.getLogger(CompanyEOController.class);

    @Autowired
    private CompanyEOService companyEOService;

	@ApiOperation(value = "|CompanyEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("company:company:page")
    public ResponseMessage<PageInfo<CompanyEO>> page(CompanyEOPage page) throws Exception {
        List<CompanyEO> rows = companyEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CompanyEO|查询")
    @GetMapping("")
    @RequiresPermissions("company:company:list")
    public ResponseMessage<List<CompanyEO>> list(CompanyEOPage page) throws Exception {
        return Result.success(companyEOService.queryByList(page));
	}

    @ApiOperation(value = "|CompanyEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("company:company:get")
    public ResponseMessage<CompanyEO> find(@PathVariable String id) throws Exception {
        return Result.success(companyEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CompanyEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("company:company:save")
    public ResponseMessage<CompanyEO> create(@RequestBody CompanyEO companyEO) throws Exception {
        companyEOService.insertSelective(companyEO);
        return Result.success(companyEO);
    }

    @ApiOperation(value = "|CompanyEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("company:company:update")
    public ResponseMessage<CompanyEO> update(@RequestBody CompanyEO companyEO) throws Exception {
        companyEOService.updateByPrimaryKeySelective(companyEO);
        return Result.success(companyEO);
    }

    @ApiOperation(value = "|CompanyEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("company:company:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        companyEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_COMPANY where id = {}", id);
        return Result.success();
    }

}
