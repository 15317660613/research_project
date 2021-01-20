package com.adc.da.category.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.category.entity.MyCategoryEO;
import com.adc.da.category.service.DeploymentCategoryService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/category/deploymentCategory")
@Api(tags = "|流程分组模块|")
public class MyDeploymentCategoryController extends BaseController<MyCategoryEO> {

    @Autowired
    private DeploymentCategoryService service;

    @ApiOperation(value = "|DeploymentCategory|查询")
    @GetMapping("")
//    @RequiresPermissions("category:modelCategory:list")
    public ResponseMessage<List<MyCategoryEO>> list() {
        return Result.success(service.getList(100, 1, null, true));
    }

}
