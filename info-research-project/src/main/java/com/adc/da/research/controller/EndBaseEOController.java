package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.EndBaseEO;
import com.adc.da.research.page.EndBaseEOPage;
import com.adc.da.research.service.EndBaseEOService;
import com.adc.da.research.service.HiBaseEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/endBase")
@Api(tags = "|科研类项目模块-结项|")
@Slf4j
public class EndBaseEOController extends BaseController<EndBaseEO> {

    @Autowired
    private EndBaseEOService endBaseEOService;

    @Autowired
    private HiBaseEOService hiBaseEOService;

    @ApiOperation(value = "|EndBaseEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:endBase:list")
    public ResponseMessage<List<EndBaseEO>> list(EndBaseEOPage page) throws Exception {
        return Result.success(endBaseEOService.queryByList(page));
    }

    @ApiOperation(value = "|EndBaseEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:endBase:update")
    public ResponseMessage<EndBaseEO> update(@RequestBody EndBaseEO endBaseEO) throws Exception {
        endBaseEOService.updateByPrimaryKeySelective(endBaseEO);
        return Result.success(endBaseEO);
    }

    @ApiOperation(value = "|HiBaseEO|获取BusinessKey")
    @GetMapping("/lastKey/{projectId}")
    public ResponseMessage<String> findByProjectId(@PathVariable String projectId, String procInstId) {
        String result;
        if (StringUtils.isNotEmpty(procInstId)) {
            result = hiBaseEOService.getBusinessKeyByProcInstId(procInstId);
        } else {
            result = endBaseEOService.getLastBusinessKey(projectId);
        }
        return Result.success(result);
    }

    @ApiOperation(value = "检验表单填写情况")
    @GetMapping("/check/{businessKey}")
    public ResponseMessage<String> check(@PathVariable String businessKey) {
        String result = endBaseEOService.check(businessKey);

        if (StringUtils.isEmpty(result)) {
            return Result.success(result);
        } else {
            return Result.error("r", result, result);
        }
    }
}
