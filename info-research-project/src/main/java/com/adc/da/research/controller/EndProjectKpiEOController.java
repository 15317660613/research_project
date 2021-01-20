package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.EndProjectKpiEO;
import com.adc.da.research.service.EndProjectKpiEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/endProjectKpi")
@Api(tags = "|科研类项目模块-结项|")
public class EndProjectKpiEOController extends BaseController<EndProjectKpiEO> {

    @Autowired
    private EndProjectKpiEOService endProjectKpiEOService;

    @ApiOperation(value = "|EndProjectKpiEO|查询")
    @GetMapping("")
    //  @RequiresPermissions("research:endProjectKpi:list")
    public ResponseMessage<List<EndProjectKpiEO>> list(String procBusinessKey) throws Exception {
        return Result.success(Collections.singletonList(endProjectKpiEOService.selectByPrimaryKey(procBusinessKey)));
    }

    @ApiOperation(value = "|EndProjectKpiEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:endProjectKpi:update")
    public ResponseMessage<EndProjectKpiEO> update(@RequestBody EndProjectKpiEO endProjectKpiEO) throws Exception {
        endProjectKpiEOService.updateByPrimaryKeySelective(endProjectKpiEO);
        return Result.success(endProjectKpiEO);
    }

}
