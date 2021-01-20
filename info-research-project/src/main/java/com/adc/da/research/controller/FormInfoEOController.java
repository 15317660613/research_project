package com.adc.da.research.controller;

import com.adc.da.research.service.FormInfoEOService;
import com.adc.da.research.vo.FormInfoVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基本信息模块
 */
@RestController
@RequestMapping("/${restPath}/research/formInfo")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class FormInfoEOController {

    @Autowired
    private FormInfoEOService formInfoEOService;

    @ApiOperation(value = "|InfoEO|详情")
    @GetMapping("/{researchProjectId}")
    ////@RequiresPermissions"research:info:get")
    public ResponseMessage<FormInfoVO> find(@PathVariable String researchProjectId) {
        return Result.success(formInfoEOService.getProjectInfo(researchProjectId));
    }

}
