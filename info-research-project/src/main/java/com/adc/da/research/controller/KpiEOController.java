package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.KpiEO;
import com.adc.da.research.page.KpiEOPage;
import com.adc.da.research.service.KpiEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
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
@RequestMapping("/${restPath}/research/kpi")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class KpiEOController extends BaseController<KpiEO> {

    @Autowired
    private KpiEOService kpiEOService;

    @ApiOperation(value = "|KpiEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions"research:kpi:page")
    public ResponseMessage<PageInfo<KpiEO>> page(KpiEOPage page) throws Exception {
        List<KpiEO> rows = kpiEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|KpiEO|查询")
    @GetMapping("")
    //@RequiresPermissions"research:kpi:list")
    public ResponseMessage<List<KpiEO>> list(KpiEOPage page) throws Exception {
        return Result.success(kpiEOService.queryByList(page));
    }

    @ApiOperation(value = "|KpiEO|详情")
    @GetMapping("/{researchProjectId}")
    //@RequiresPermissions"research:kpi:get")
    public ResponseMessage<KpiEO> find(@PathVariable String researchProjectId) throws Exception {
        return Result.success(kpiEOService.selectByPrimaryKey(researchProjectId));
    }

    @ApiOperation(value = "|KpiEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"research:kpi:save")
    public ResponseMessage<KpiEO> create(@RequestBody KpiEO kpiEO) throws Exception {
        kpiEOService.insertSelective(kpiEO);
        return Result.success(kpiEO);
    }

    @ApiOperation(value = "|KpiEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"research:kpi:update")
    public ResponseMessage<KpiEO> update(@RequestBody KpiEO kpiEO) throws Exception {
        kpiEOService.updateByPrimaryKeySelective(kpiEO);
        return Result.success(kpiEO);
    }

    @ApiOperation(value = "|KpiEO|删除")
    @DeleteMapping("/{researchProjectId}")
    //@RequiresPermissions"research:kpi:delete")
    public ResponseMessage delete(@PathVariable String researchProjectId) throws Exception {
        kpiEOService.deleteByPrimaryKey(researchProjectId);
        log.info("delete from RS_PROJECT_KPI where researchProjectId = {}", researchProjectId);
        return Result.success();
    }

}
