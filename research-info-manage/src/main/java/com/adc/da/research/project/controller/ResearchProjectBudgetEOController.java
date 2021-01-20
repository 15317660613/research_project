package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ResearchProjectBudgetEO;
import com.adc.da.research.project.page.ResearchProjectBudgetEOPage;
import com.adc.da.research.project.service.ResearchProjectBudgetEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/researchProjectBudget")
@Api(tags = "科研系统|合同模板|ResearchProjectBudgetEO")
public class ResearchProjectBudgetEOController extends BaseController<ResearchProjectBudgetEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectBudgetEOController.class);

    @Autowired
    private ResearchProjectBudgetEOService researchProjectBudgetEOService;

	@ApiOperation(value = "|ResearchProjectBudgetEO|分页查询")
    @GetMapping("/page")
   // @RequiresPermissions("project:researchProjectBudget:page")
    public ResponseMessage<PageInfo<ResearchProjectBudgetEO>> page(ResearchProjectBudgetEOPage page) throws Exception {
        List<ResearchProjectBudgetEO> rows = researchProjectBudgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }
    @ApiOperation(value = "|ResearchProjectBudgetEO|分科目查询")
    @GetMapping("/queryByArr")
    // @RequiresPermissions("project:researchProjectBudget:page")
    public ResponseMessage<Map<String,List<ResearchProjectBudgetEO>>> queryByArr(ResearchProjectBudgetEOPage page) throws Exception {
        Map<String,List<ResearchProjectBudgetEO>> rows = researchProjectBudgetEOService.queryByArr(page);
        return Result.success( rows);
    }
	@ApiOperation(value = "|ResearchProjectBudgetEO|查询")
    @GetMapping("")
   // @RequiresPermissions("project:researchProjectBudget:list")
    public ResponseMessage<List<ResearchProjectBudgetEO>> list(ResearchProjectBudgetEOPage page) throws Exception {
        return Result.success(researchProjectBudgetEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResearchProjectBudgetEO|详情")
    @GetMapping("/{id}")
   // @RequiresPermissions("project:researchProjectBudget:get")
    public ResponseMessage<ResearchProjectBudgetEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchProjectBudgetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchProjectBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("project:researchProjectBudget:save")
    public ResponseMessage<ResearchProjectBudgetEO> create(@RequestBody ResearchProjectBudgetEO researchProjectBudgetEO) throws Exception {
        researchProjectBudgetEOService.insertSelective(researchProjectBudgetEO);
        return Result.success(researchProjectBudgetEO);
    }

    @ApiOperation(value = "|ResearchProjectBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchProjectBudget:update")
    public ResponseMessage<ResearchProjectBudgetEO> update(@RequestBody ResearchProjectBudgetEO researchProjectBudgetEO) throws Exception {
        researchProjectBudgetEOService.updateByPrimaryKeySelective(researchProjectBudgetEO);
        return Result.success(researchProjectBudgetEO);
    }

    @ApiOperation(value = "|ResearchProjectBudgetEO|删除")
    @DeleteMapping("/{id}")
   // @RequiresPermissions("project:researchProjectBudget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchProjectBudgetEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RESEARCH_PROJECT_BUDGET where id = {}", id);
        return Result.success();
    }

}
