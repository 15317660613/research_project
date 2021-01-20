package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.EndProjectKpiDeliverableEO;
import com.adc.da.research.page.EndProjectKpiDeliverableEOPage;
import com.adc.da.research.service.EndProjectKpiDeliverableEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
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
@RequestMapping("/${restPath}/research/endProjectKpiDeliverable")
@Api(tags = "|科研类项目模块-结项|")
public class EndProjectKpiDeliverableEOController extends BaseController<EndProjectKpiDeliverableEO> {

    private static final Logger logger = LoggerFactory.getLogger(EndProjectKpiDeliverableEOController.class);

    @Autowired
    private EndProjectKpiDeliverableEOService service;

    @ApiOperation(value = "|EndProjectKpiDeliverableEO|分页查询")
    @GetMapping("/page")
    //  @RequiresPermissions("research:endProjectKpiDeliverable:page")
    public ResponseMessage<PageInfo<EndProjectKpiDeliverableEO>> page(EndProjectKpiDeliverableEOPage page)
        throws Exception {
        List<EndProjectKpiDeliverableEO> rows = service.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|EndProjectKpiDeliverableEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:endProjectKpiDeliverable:save")
    public ResponseMessage<EndProjectKpiDeliverableEO> create(
        @RequestBody EndProjectKpiDeliverableEO endProjectKpiDeliverableEO) throws Exception {
        endProjectKpiDeliverableEO.setId(UUID.randomUUID10());
        service.insertSelective(endProjectKpiDeliverableEO);
        return Result.success(endProjectKpiDeliverableEO);
    }

    @ApiOperation(value = "|EndProjectKpiDeliverableEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:endProjectKpiDeliverable:update")
    public ResponseMessage<EndProjectKpiDeliverableEO> update(
        @RequestBody EndProjectKpiDeliverableEO endProjectKpiDeliverableEO) throws Exception {
        service.updateByPrimaryKeySelective(endProjectKpiDeliverableEO);
        return Result.success(endProjectKpiDeliverableEO);
    }

    @ApiOperation(value = "|EndProjectKpiDeliverableEO|删除")
    @DeleteMapping("/{id}")
    //  @RequiresPermissions("research:endProjectKpiDeliverable:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        service.deleteByPrimaryKey(id);
        logger.info("delete from RS_END_PROJECT_KPI_DELIVERABLE where id = {}", id);
        return Result.success();
    }

}
