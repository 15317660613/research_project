package com.adc.da.budget.controller;

import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.service.BudgetProjectDTOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${restPath}/budget/budget")
@Api(tags = "|BudgetEO|")
@Slf4j
public class BudgetProjectDTOController {

    @Autowired
    private BudgetProjectDTOService budgetProjectDTOService;

    @ApiOperation(value = "业务下的项目-条件查询-分页")
    @GetMapping("/getProjectsByTips4Page")
    public ResponseMessage<PageDTO> getProjects4Page(@RequestParam("pageIndex") Integer pageIndex,
        @RequestParam("pageSize") Integer pageSize,
        @RequestParam("bussinessId") String bussinessId,
        @RequestParam(value = "projectName", required = false) String projectName,
        @RequestParam(value = "projectStatus", required = false) String projectStatus,
        @RequestParam(value = "projectManager", required = false) String projectManager) {
        return Result.success(budgetProjectDTOService.getProjectsDTO(pageIndex, pageSize, bussinessId, projectName,
            projectStatus, projectManager));
    }

}
