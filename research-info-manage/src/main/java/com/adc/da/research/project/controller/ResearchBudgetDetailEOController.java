package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ResearchBudgetDetailEO;
import com.adc.da.research.project.page.ResearchBudgetDetailEOPage;
import com.adc.da.research.project.service.ResearchBudgetDetailEOService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/researchBudgetDetail")
@Api(tags = "科研系统|项目管理-->经费测算依据|ResearchBudgetDetailEO")
public class ResearchBudgetDetailEOController extends BaseController<ResearchBudgetDetailEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchBudgetDetailEOController.class);

    @Autowired
    private ResearchBudgetDetailEOService researchBudgetDetailEOService;

	@ApiOperation(value = "|ResearchBudgetDetailEO|分页查询")
    @GetMapping("/page")
   // @RequiresPermissions("project:researchBudgetDetail:page")
    public ResponseMessage<PageInfo<ResearchBudgetDetailEO>> page(ResearchBudgetDetailEOPage page) throws Exception {
        List<ResearchBudgetDetailEO> rows = researchBudgetDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResearchBudgetDetailEO|查询")
    @GetMapping("")
   // @RequiresPermissions("project:researchBudgetDetail:list")
    public ResponseMessage<List<ResearchBudgetDetailEO>> list(ResearchBudgetDetailEOPage page) throws Exception {
        return Result.success(researchBudgetDetailEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResearchBudgetDetailEO|详情")
    @GetMapping("/{id}")
  //  @RequiresPermissions("project:researchBudgetDetail:get")
    public ResponseMessage<ResearchBudgetDetailEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchBudgetDetailEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchBudgetDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchBudgetDetail:save")
    public ResponseMessage<ResearchBudgetDetailEO> create(@RequestBody ResearchBudgetDetailEO researchBudgetDetailEO) throws Exception {
        researchBudgetDetailEOService.saveResearchBudgetDetailEO(researchBudgetDetailEO);
        return Result.success(researchBudgetDetailEO);
    }

    @ApiOperation(value = "|ResearchBudgetDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchBudgetDetail:update")
    public ResponseMessage<ResearchBudgetDetailEO> update(@RequestBody ResearchBudgetDetailEO researchBudgetDetailEO) throws Exception {
        researchBudgetDetailEOService.updateResearchBudgetDetailEO(researchBudgetDetailEO);
        return Result.success(researchBudgetDetailEO);
    }

    @ApiOperation(value = "|ResearchBudgetDetailEO|删除")
    @DeleteMapping("/{id}")
   // @RequiresPermissions("project:researchBudgetDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchBudgetDetailEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RESEARCH_BUDGET_DETAIL where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ResearchBudgetDetailEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<ResearchBudgetDetailEO> batchInsertSelective(@RequestBody List<ResearchBudgetDetailEO> researchBudgetDetailEOS) throws Exception {
        researchBudgetDetailEOService.batchInsertSelective(researchBudgetDetailEOS);
        return Result.success();
    }

}
