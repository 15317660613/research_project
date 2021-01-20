package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.KpiDeliverableEO;
import com.adc.da.research.page.KpiDeliverableEOPage;
import com.adc.da.research.service.KpiDeliverableEOService;
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
@RequestMapping("/${restPath}/research/kpiDeliverable")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class KpiDeliverableEOController extends BaseController<KpiDeliverableEO> {

    @Autowired
    private KpiDeliverableEOService kpiDeliverableEOService;

    @ApiOperation(value = "|KpiDeliverableEO|分页查询")
    @GetMapping("/page")
    ////@RequiresPermissions"research:kpiDeliverable:page")
    public ResponseMessage<PageInfo<KpiDeliverableEO>> page(KpiDeliverableEOPage page) throws Exception {
        List<KpiDeliverableEO> rows = kpiDeliverableEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|KpiDeliverableEO|查询")
    @GetMapping("")
    ////@RequiresPermissions"research:kpiDeliverable:list")
    public ResponseMessage<List<KpiDeliverableEO>> list(KpiDeliverableEOPage page) throws Exception {
        return Result.success(kpiDeliverableEOService.queryByList(page));
    }

    @ApiOperation(value = "|KpiDeliverableEO|详情")
    @GetMapping("/{id}")
    ////@RequiresPermissions"research:kpiDeliverable:get")
    public ResponseMessage<KpiDeliverableEO> find(@PathVariable String id) throws Exception {
        return Result.success(kpiDeliverableEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|KpiDeliverableEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:kpiDeliverable:save")
    public ResponseMessage<KpiDeliverableEO> create(@RequestBody KpiDeliverableEO kpiDeliverableEO) {
        kpiDeliverableEO.setId(UUID.randomUUID10());
        kpiDeliverableEOService.insertSelective(kpiDeliverableEO);
        return Result.success(kpiDeliverableEO);
    }

    @ApiOperation(value = "|KpiDeliverableEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:kpiDeliverable:update")
    public ResponseMessage<KpiDeliverableEO> update(@RequestBody KpiDeliverableEO kpiDeliverableEO) throws Exception {
        kpiDeliverableEOService.updateByPrimaryKeySelective(kpiDeliverableEO);
        return Result.success(kpiDeliverableEO);
    }

    @ApiOperation(value = "|KpiDeliverableEO|删除")
    @DeleteMapping("/{id}")
    ////@RequiresPermissions"research:kpiDeliverable:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        kpiDeliverableEOService.deleteByPrimaryKey(id);
        log.info("delete from RS_PROJECT_KPI_DELIVERABLE where id = {}", id);
        return Result.success();
    }

}
