package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.EndCapitalExpenditureEO;
import com.adc.da.research.service.EndCapitalExpenditureEOService;
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
@RequestMapping("/${restPath}/research/endCapitalExpenditure")
@Api(tags = "|科研类项目模块-结项|")
public class EndExpenditureEOController extends BaseController<EndCapitalExpenditureEO> {

    @Autowired
    private EndCapitalExpenditureEOService endCapitalExpenditureEOService;

    @ApiOperation(value = "|EndCapitalExpenditureEO|查询")
    @GetMapping("")
//       //  @RequiresPermissions("research:endCapitalExpenditure:list")
    public ResponseMessage<List<EndCapitalExpenditureEO>> list(String procBusinessKey) {
        return Result.success(endCapitalExpenditureEOService.getList(procBusinessKey));
    }

    @ApiOperation(value = "|EndCapitalExpenditureEO|修改-批量")
    @PutMapping(value = "/multi", consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:capitalExpenditure:update")
    public ResponseMessage<List<EndCapitalExpenditureEO>> updateMulti(
        @RequestBody List<EndCapitalExpenditureEO> capitalExpenditureEOList)
        throws Exception {
        for (EndCapitalExpenditureEO capitalExpenditureEO : capitalExpenditureEOList) {
            endCapitalExpenditureEOService.updateByPrimaryKeySelective(capitalExpenditureEO);
        }
        return Result.success(capitalExpenditureEOList);
    }

}
