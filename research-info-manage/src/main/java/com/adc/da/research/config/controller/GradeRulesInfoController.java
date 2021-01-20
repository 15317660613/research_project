package com.adc.da.research.config.controller;

import com.adc.da.research.config.entity.GradeRulesInfoEO;
import com.adc.da.research.config.entity.GradingRulesEO;
import com.adc.da.research.config.service.GradeRulesInfoService;
import com.adc.da.research.config.vo.GradeRulesInfoVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @description
 * @date 2020/10/23 10:03
 * @auth zhn
 */
@RestController
@RequestMapping("/${restPath}/research/gradeRulesInfo")
@Api(tags = "科研系统|评分规则|GradeRulesInfoController")
public class GradeRulesInfoController {
    @Autowired
    private GradeRulesInfoService gradeRulesInfoService;

    @ApiOperation(value = "|新增或修改评分规则|")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:gradingRules:save")
    public ResponseMessage<GradingRulesEO> create(@RequestBody List<GradeRulesInfoVO> gradeRulesInfoVOS) throws Exception {
        gradeRulesInfoService.insertOrUpdate(gradeRulesInfoVOS);
        return Result.success();
    }

    @ApiOperation(value = "|批量删除--评分规则|")
    @DeleteMapping("/batchDelete/{ids}")
//    @RequiresPermissions("research:gradingRules:delete")
    public ResponseMessage batchDelete(@NotNull @PathVariable("ids") List<String> ids) throws Exception {
        gradeRulesInfoService.batchDeleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "查看单条详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:ratingRules:get")
    public ResponseMessage<List<GradeRulesInfoEO>> find(@PathVariable String id) throws Exception {
        List<GradeRulesInfoEO> gradeRulesInfoEOS = gradeRulesInfoService.selectById(id);
        return Result.success(gradeRulesInfoEOS);
    }

    @ApiOperation(value = "查看单条详情 (根据project id)")
    @GetMapping("/projectId/{id}")
//    @RequiresPermissions("research:ratingRules:get")
    public ResponseMessage<List<GradeRulesInfoEO>> findByProjectId(@PathVariable("id") String projectId) throws Exception {
        List<GradeRulesInfoEO> gradeRulesInfoEOS = gradeRulesInfoService.selectByProjectId(projectId);
        return Result.success(gradeRulesInfoEOS);
    }

}
