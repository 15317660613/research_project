package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.HiInfoEO;
import com.adc.da.research.page.HiInfoEOPage;
import com.adc.da.research.service.HiInfoEOService;
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
@RequestMapping("/${restPath}/research/hiProjectInfo")
@Api(tags = "|科研类项目模块-变更流程|")
public class HiInfoEOController extends BaseController<HiInfoEO> {

    @Autowired
    private HiInfoEOService hiInfoEOService;

    @ApiOperation(value = "|HiProjectInfoEO|查询")
    @GetMapping("")
    ////@RequiresPermissions("research:hiProjectInfo:list")
    public ResponseMessage<List<HiInfoEO>> list(String procBusinessKey) throws Exception {
        HiInfoEOPage page = new HiInfoEOPage();
        page.setProcBusinessKey(procBusinessKey);
        return Result.success(hiInfoEOService.queryByList(page));
    }

    @ApiOperation(value = "|HiProjectInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions("research:hiProjectInfo:update")
    public ResponseMessage<HiInfoEO> update(@RequestBody HiInfoEO hiProjectInfoEO) {

        return Result.success(hiInfoEOService.updateAndSetMask(hiProjectInfoEO));
    }

}
