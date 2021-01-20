package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.SummaryEO;
import com.adc.da.research.page.SummaryEOPage;
import com.adc.da.research.service.SummaryEOService;
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
@RequestMapping("/${restPath}/research/summary")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class SummaryEOController extends BaseController<SummaryEO> {

    @Autowired
    private SummaryEOService summaryEOService;

    @ApiOperation(value = "|SummaryEO|分页查询")
    @GetMapping("/page")
    ////@RequiresPermissions"research:summary:page")
    public ResponseMessage<PageInfo<SummaryEO>> page(SummaryEOPage page) throws Exception {
        List<SummaryEO> rows = summaryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|SummaryEO|查询")
    @GetMapping("")
    ////@RequiresPermissions"research:summary:list")
    public ResponseMessage<List<SummaryEO>> list(SummaryEOPage page) throws Exception {
        return Result.success(summaryEOService.queryByList(page));
    }

    @ApiOperation(value = "|SummaryEO|详情")
    @GetMapping("/{id}")
    ////@RequiresPermissions"research:summary:get")
    public ResponseMessage<SummaryEO> find(@PathVariable String id) throws Exception {
        return Result.success(summaryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|SummaryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:summary:save")
    public ResponseMessage<SummaryEO> create(@RequestBody SummaryEO summaryEO) throws Exception {
        summaryEO.setId(UUID.randomUUID10());
        summaryEOService.insertSelective(summaryEO);
        return Result.success(summaryEO);
    }

    @ApiOperation(value = "|SummaryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:summary:update")
    public ResponseMessage<SummaryEO> update(@RequestBody SummaryEO summaryEO) throws Exception {
        summaryEOService.updateByPrimaryKeySelective(summaryEO);
        return Result.success(summaryEO);
    }

    @ApiOperation(value = "|SummaryEO|删除")
    @DeleteMapping("/{id}")
    ////@RequiresPermissions"research:summary:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        summaryEOService.deleteByPrimaryKey(id);
        log.info("delete from RS_PROJECT_SUMMARY where id = {}", id);
        return Result.success();
    }

}
