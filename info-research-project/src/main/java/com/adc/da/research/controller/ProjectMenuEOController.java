package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.MenuEO;
import com.adc.da.research.service.ProjectMenuEOService;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/${restPath}/research/menu")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class ProjectMenuEOController extends BaseController<MenuEO> {

    @Autowired
    private ProjectMenuEOService projectMenuEOService;

    @ApiOperation(value = "|MenuEO|查询")
    @GetMapping("")
    ////@RequiresPermissions"research:menu:list")
    public ResponseMessage<Map<String, Object>> list(String topNodeId) {
        Map<String, Object> resultMap = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        resultMap.put("thisYear", String.valueOf(year));
        resultMap.put("data", projectMenuEOService.queryByListWithLevel(topNodeId));

        return Result.success(resultMap);
    }

    @ApiOperation(value = "|MenuEO|详情")
    @GetMapping("/{id}")
    ////@RequiresPermissions"research:menu:get")
    public ResponseMessage<MenuEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectMenuEOService.selectByPrimaryKey(id));
    }

}
