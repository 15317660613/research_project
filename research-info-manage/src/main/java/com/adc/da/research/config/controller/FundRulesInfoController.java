package com.adc.da.research.config.controller;

import com.adc.da.research.config.entity.FundRulesEO;
import com.adc.da.research.config.entity.FundRulesInfoEO;
import com.adc.da.research.config.service.FundRulesInfoService;
import com.adc.da.research.config.vo.FundRulesInfoVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @description
 * @date 2020/10/26 9:04
 * @auth zhn
 */
@RestController
@RequestMapping("/${restPath}/research/expenditureRulesInfo")
@Api(tags = "科研系统||经费科目规则信息|FundRulesInfoController")
public class FundRulesInfoController {

    @Autowired
    private FundRulesInfoService fundRulesInfoService;


    @ApiOperation(value = "|经费科目规则新增和修改|")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("config:fundRules:save")
    public ResponseMessage<FundRulesEO> create(@RequestBody List<FundRulesInfoVO> fundRulesInfoVOS) throws Exception {
        fundRulesInfoService.insertOrUpdate(fundRulesInfoVOS);
        return Result.success();
    }

    @ApiOperation(value = "|批量删除--经费科目规则|")
    @DeleteMapping("/batchDelete/{ids}")
//    @RequiresPermissions("research:gradingRules:delete")
    public ResponseMessage batchDelete(@NotNull @PathVariable("ids") List<String> ids) throws Exception {
        fundRulesInfoService.batchDeleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "查看单条详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:ratingRules:get")
    public ResponseMessage<List<FundRulesInfoEO>> find(@PathVariable String id) throws Exception {
        List<FundRulesInfoEO> gradeRulesInfoEOS = fundRulesInfoService.selectById(id);
        return Result.success(gradeRulesInfoEOS);
    }
}
