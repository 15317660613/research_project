package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.EndProjectSummaryEO;
import com.adc.da.research.service.EndProjectSummaryEOService;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/endProjectSummary")
@Api(tags = "|科研类项目模块-结项|")
public class EndProjectSummaryEOController extends BaseController<EndProjectSummaryEO> {

    @Autowired
    private EndProjectSummaryEOService endProjectSummaryEOService;

    @ApiOperation(value = "|EndProjectSummaryEO|查询")
    @GetMapping("")
    //  @RequiresPermissions("research:endProjectSummary:list")
    public ResponseMessage<List<EndProjectSummaryEO>> list(String procBusinessKey, String researchProjectId) {

        return Result.success(endProjectSummaryEOService.getSummary(procBusinessKey, researchProjectId));
    }

    @ApiOperation(value = "|EndProjectSummaryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:endProjectSummary:update")
    public ResponseMessage<EndProjectSummaryEO> update(@RequestBody EndProjectSummaryEO endProjectSummaryEO)
        throws Exception {
        endProjectSummaryEOService.updateByPrimaryKeySelective(endProjectSummaryEO);
        return Result.success(endProjectSummaryEO);
    }

}
